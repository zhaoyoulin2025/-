import request from '@/utils/request'

// 查询客户信息列表
export function listClient(query) {
  return request({
    url: '/system/client/list',
    method: 'get',
    params: query
  })
}




// 查询客户信息详细
export function getClient(id) {
  return request({
    url: '/system/client/' + id,
    method: 'get'
  })
}

// 新增客户信息
export function addClient(data) {
  return request({
    url: '/system/client',
    method: 'post',
    data: data
  })
}

// 修改客户信息
export function updateClient(data) {
  return request({
    url: '/system/client',
    method: 'put',
    data: data
  })
}

// 删除客户信息
export function delClient(id) {
  return request({
    url: '/system/client/' + id,
    method: 'delete'
  })
}

// 查询用户列表
export function listUser(query) {
  return request({
    url: '/system/user/list',
    method: 'get',
    params: query
  })
}

// 批量设置跟进人员的接口
export function BatchSetProple(data) {
  return request({
    url: '/system/client/addFollowers',
    method: 'post',
    data: data
  })
}

// 查询客户等级数据
export function clientLevel(query) {
  return request({
    url: '/system/dict/data/list',
    method: 'get',
    params: query
  })
}

//新增跟进记录接口
export function clientFollowData(data) {
  return request({
    url: '/system/up',
    method: 'post',
    data: data
  })
}

// 查询调查问卷列表数据接口
export function getQuestionListData(query) {
  return request({
    url: '/system/form/client',
    method: 'get',
    params: query
  })
}
