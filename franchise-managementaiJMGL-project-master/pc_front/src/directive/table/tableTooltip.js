// src/directive/tableTooltip.js
export default {
  // 指令的 bind 钩子：当指令绑定到元素时触发
  bind(el, binding, vnode) {
    // 找到表格容器
    const table = el.closest('.el-table');
    if (!table) return;

    // 监听表格DOM变化（确保数据加载后初始化）
    const observer = new MutationObserver(() => {
      initTooltip(el, vnode);
    });
    observer.observe(table, { childList: true, subtree: true });

    // 首次初始化
    initTooltip(el, vnode);
  }
};

// 初始化tooltip的辅助函数
function initTooltip(tableEl, vnode) {
  // 遍历所有单元格
  const cells = tableEl.querySelectorAll('.el-table__cell');
  cells.forEach(cell => {
    const text = cell.textContent.trim();
    if (!text) return;

    // 判断内容是否溢出（有省略号时才显示tooltip）
    const hasEllipsis = cell.offsetWidth < cell.scrollWidth;
    if (hasEllipsis) {
      // 使用当前组件的Vue实例创建tooltip（避免重复实例化）
      const tooltip = vnode.context.$createElement('el-tooltip', {
        props: {
          effect: 'dark',
          content: text,
          placement: 'top',
          trigger: 'hover'
        }
      }, [
        vnode.context.$createElement('span', text)
      ]);

      // 渲染并替换单元格内容
      const tooltipVNode = tooltip;
      const tooltipEl = vnode.context.$mount(tooltipVNode);
      cell.innerHTML = '';
      cell.appendChild(tooltipEl.$el);
    }
  });
}
