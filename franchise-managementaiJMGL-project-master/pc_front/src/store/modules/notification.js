// Vue2的Vuex模块写法
const state = {
  count: 0,
  list: []
}

const mutations = {
  INCREMENT_COUNT(state) {
    state.count++
  },
  RESET_COUNT(state) {
    state.count = 0
  },
  ADD_NOTIFICATION(state, notification) {
    state.list.unshift(notification)
    // 限制通知数量
    if (state.list.length > 50) {
      state.list.pop()
    }
  }
}

const actions = {
  incrementCount({ commit }) {
    commit('INCREMENT_COUNT')
  },
  resetCount({ commit }) {
    commit('RESET_COUNT')
  },
  addNotification({ commit }, notification) {
    commit('ADD_NOTIFICATION', notification)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
