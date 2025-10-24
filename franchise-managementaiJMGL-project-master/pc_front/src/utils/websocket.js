class WebSocketUtil {
  constructor() {
    this.messageCache = []; // 缓存先发消息
    this.socket = null;
    this.isConnect = false;
    this.reconnectTimer = null;
    this.heartBeatTimer = null;
    this.messageCallbacks = new Map();
    this.globalCallbacks = [];
    this.url = '';
    this.userId = null;
    this.token = null;
    this.reconnectCount = 0;
    this.maxReconnectCount = 5;
  }

  init(userId, token) {
    if (this.isConnect) return;

    this.userId = userId;
    this.token = token;
    // 自动根据当前页面协议选择ws或wss
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';

    // 从环境变量获取WebSocket基础地址，若无则自动构建
    let baseUrl = process.env.VUE_APP_WS_BASE_URL;
    if (!baseUrl) {
      // 移除协议部分，使用自动选择的协议
      const host = window.location.hostname;
      // 判断是否为localhost，如果不是则不添加端口
      const isLocalhost = host === 'localhost' || host === '127.0.0.1';
      let portPart = '';

      if (isLocalhost) {
        // 只有本地环境才使用端口
        const port = process.env.VUE_APP_WS_PORT || '8089';
        portPart = `:${port}`;
      }

      baseUrl = `${protocol}//${host}${portPart}/manage/wsServer/${userId}`;
    } else {
      // 如果环境变量提供的地址包含协议，则使用它，否则补充自动选择的协议
      if (!baseUrl.startsWith('ws:')) {
        baseUrl = `${protocol}//${baseUrl.replace(/^\/\//, '')}`;
      }
    }

    // 构建带用户认证参数的WebSocket URL
    this.url = `${baseUrl}?userId=${encodeURIComponent(userId)}`;

    try {
      this.socket = new WebSocket(this.url);
      console.log('正在连接WebSocket:', this.url);

      this.socket.onopen = () => {
        console.log('WebSocket连接成功');
        this.isConnect = true;
        this.reconnectCount = 0;
        this.startHeartBeat();
        clearTimeout(this.reconnectTimer);
      };

      this.socket.onmessage = (e) => {
        this.heartBeatTimer && clearTimeout(this.heartBeatTimer);
        this.startHeartBeat();

        try {
          const message = JSON.parse(e.data);

          if (message.type === 'heartbeat') return;

          console.log('收到WebSocket消息:', message);

          // 处理消息分发
          if (this.globalCallbacks.length > 0) {
            this.globalCallbacks.forEach(cb => cb(message));
          } else {
            this.messageCache.push(message); // 缓存消息
          }

          if (message.type && this.messageCallbacks.has(message.type)) {
            this.messageCallbacks.get(message.type).forEach(cb => cb(message));
          }
        } catch (error) {
          // 处理非JSON消息
          console.warn('收到非JSON格式消息:', e.data);

          // 特殊处理"连接成功"消息
          if (e.data === "连接成功") {
            console.log('WebSocket连接已确认');
            return;
          }

          // 触发原始数据回调
          this.globalCallbacks.forEach(cb => cb({
            raw: e.data,
            isRaw: true
          }));
        }
      };

      this.socket.onclose = (e) => {
        console.log(`WebSocket连接关闭: 代码=${e.code}, 原因=${e.reason}`);
        this.isConnect = false;
        this.cleanupTimers();
        this.reconnect();
      };

      this.socket.onerror = (e) => {
        console.error('WebSocket连接错误:', e);
        this.isConnect = false;
        this.cleanupTimers();
        this.reconnect();
      };
    } catch (error) {
      console.error('创建WebSocket实例失败:', error);
      this.reconnect();
    }
  }

  // 清理定时器
  cleanupTimers() {
    this.heartBeatTimer && clearTimeout(this.heartBeatTimer);
    this.reconnectTimer && clearTimeout(this.reconnectTimer);
  }

  reconnect() {
    if (this.isConnect || this.reconnectCount >= this.maxReconnectCount) return;

    this.reconnectCount++;
    const delay = Math.min(30000, 1000 * Math.pow(2, this.reconnectCount)); // 指数退避策略
    console.log(`尝试重新连接WebSocket(${this.reconnectCount}/${this.maxReconnectCount})，将在${delay}ms后尝试...`);

    this.reconnectTimer = setTimeout(() => {
      if (this.userId && this.token) {
        this.init(this.userId, this.token);
      }
    }, delay);
  }

  // 注册特定类型消息的回调
  onMessageType(type, callback) {
    if (!this.messageCallbacks.has(type)) {
      this.messageCallbacks.set(type, []);
    }
    this.messageCallbacks.get(type).push(callback);
  }

  // 注册全局消息回调
  onGlobalMessage(callback) {
    this.globalCallbacks.push(callback);
    // 补发缓存的消息
    this.messageCache.forEach(msg => callback(msg));
    this.messageCache = []; // 补发后清空缓存
  }

  // 移除特定类型消息的回调
  offMessageType(type, callback) {
    if (this.messageCallbacks.has(type)) {
      const callbacks = this.messageCallbacks.get(type);
      const index = callbacks.indexOf(callback);
      if (index > -1) {
        callbacks.splice(index, 1);
      }
    }
  }

  // 移除全局消息回调
  offGlobalMessage(callback) {
    const index = this.globalCallbacks.indexOf(callback);
    if (index > -1) {
      this.globalCallbacks.splice(index, 1);
    }
  }

  // 发送消息
  send(message) {
    if (this.isConnect && this.socket) {
      try {
        this.socket.send(JSON.stringify(message));
        return true;
      } catch (e) {
        console.error('发送WebSocket消息失败:', e);
        return false;
      }
    } else {
      console.warn('WebSocket未连接，消息发送失败:', message);
      return false;
    }
  }

  // 关闭连接
  close() {
    if (this.socket) {
      this.socket.close(1000, '正常关闭'); // 1000是正常关闭的代码
      this.socket = null;
      this.isConnect = false;
      this.cleanupTimers();
      console.log('WebSocket连接已关闭');
    }
  }

  // 启动心跳检测
  startHeartBeat() {
    this.heartBeatTimer && clearTimeout(this.heartBeatTimer);
    this.heartBeatTimer = setTimeout(() => {
      if (this.isConnect && this.socket) {
        this.send({ type: 'heartbeat' });
      }
    }, 30000); // 30秒一次心跳
  }

  // 获取连接状态
  getConnectionStatus() {
    return this.isConnect;
  }
}

const instance = new WebSocketUtil();
export default instance;
