<template>
  <div class="navbar">
    <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container"
      @toggleClick="toggleSideBar" />

    <breadcrumb v-if="!topNav" id="breadcrumb-container" class="breadcrumb-container" />
    <top-nav v-if="topNav" id="topmenu-container" class="topmenu-container" />

    <div class="right-menu">
      <template v-if="device !== 'mobile'">
        <div class="bell-container right-menu-item" @click.stop="toggleMessageDropdown">
          <el-badge :value="noticeList.length" :max="99" class="item" :hidden="noticeList.length === 0">
            <svg-icon icon-class="message" class="bell-icon" />
          </el-badge>
        </div>
        <search id="header-search" class="right-menu-item" />

<!--        <el-tooltip content="源码地址" effect="dark" placement="bottom">-->
<!--          <ruo-yi-git id="ruoyi-git" class="right-menu-item hover-effect" />-->
<!--        </el-tooltip>-->

        <el-tooltip content="文档地址" effect="dark" placement="bottom">
          <ruo-yi-doc id="ruoyi-doc" class="right-menu-item hover-effect" />
        </el-tooltip>

        <screenfull id="screenfull" class="right-menu-item hover-effect" />

        <el-tooltip content="布局大小" effect="dark" placement="bottom">
          <size-select id="size-select" class="right-menu-item hover-effect" />
        </el-tooltip>

      </template>

      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <img :src="avatar" class="user-avatar">
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown">
          <router-link to="/user/profile">
            <el-dropdown-item>个人中心</el-dropdown-item>
          </router-link>
          <el-dropdown-item @click.native="setting = true">
            <span>布局设置</span>
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="logout">
            <span>退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>

    <!-- 消息下拉框 -->
    <el-dialog title="消息中心" :visible.sync="dialogTableVisible" @closed="clearAllMessages">
      <el-table :data="noticeList" style="width: 100%" empty-text="暂无未读消息">
        <el-table-column property="userId" label="用户ID" width="150"></el-table-column>
        <el-table-column property="title" label="消息标题" width="200"></el-table-column>
        <el-table-column property="content" label="消息内容"></el-table-column>
        <el-table-column label="操作" width="120">
          <template slot-scope="scope">
            <el-button type="text" @click="handleAcknowledge(scope.row)">知晓</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import TopNav from '@/components/TopNav'
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import Search from '@/components/HeaderSearch'
import RuoYiGit from '@/components/RuoYi/Git'
import RuoYiDoc from '@/components/RuoYi/Doc'
import ws from '@/utils/websocket' // 导入WebSocket工具类
import  {readMeaasge}  from "@/api/system/design"

export default {
  components: {
    Breadcrumb,
    TopNav,
    Hamburger,
    Screenfull,
    SizeSelect,
    Search,
    RuoYiGit,
    RuoYiDoc
  },
  data() {
    return {
      noticeList: [],
      dialogTableVisible: false,
    };
  },
  created() {
    console.log('ws.globalCallbacks 初始长度:', ws.globalCallbacks.length);
    ws.onGlobalMessage((message) => {
      console.log('前端收到消息：', message); // 关键：确认是否收到
      console.log('消息类型：', typeof message); // 确认是否为对象
      console.log(message)
      this.noticeList.push(message);
    });
    console.log('ws.globalCallbacks 注册后长度:', ws.globalCallbacks.length);
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'device'
    ]),
    setting: {
      get() {
        return this.$store.state.settings.showSettings
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'showSettings',
          value: val
        })
      }
    },
    topNav: {
      get() {
        return this.$store.state.settings.topNav
      }
    }
  },
  methods: {
    handleAcknowledge(row){
      console.log(row.id)
      readMeaasge(row.id).then(response => {
        const index = this.noticeList.findIndex(item => item.id === row.id);
        if (index !== -1) {
          this.noticeList.splice(index, 1); // 响应式删除
        }
      }).catch(error => {
        console.error('标记消息失败:', error);
      });
    },
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    logout() {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          location.href = '/index'
        })
      }).catch(() => { })
    },
    toggleMessageDropdown() {
      this.dialogTableVisible = !this.dialogTableVisible;
    },
    clearAllMessages() {
      console.log(this.noticeList)
      // this.noticeList = [];
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, .08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .topmenu-container {
    position: absolute;
    left: 50px;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}

.bell-container {
  position: relative;
  cursor: pointer;
  display: flex;
  align-items: center;
  padding: 0 10px;
  height: 100%;

  &:hover {
    background: rgba(0, 0, 0, 0.025);
  }

  ::v-deep .el-badge {
    .el-badge__content {
      top: 10px;
    }
  }
}

.bell-icon {
  font-size: 20px;
  color: #5a5e66;
  transition: all 0.3s;
}
</style>
