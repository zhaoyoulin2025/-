import request from '@/utils/request'

// 查询客户事件列表
export function listClientEvent(query) {
  return request({
    url: '/system/clientEvent/list',
    method: 'get',
    params: query
  })
}

// 查询客户事件详细
export function getClientEvent(id) {
  return request({
    url: '/system/clientEvent/' + id,
    method: 'get'
  })
}

// 新增客户事件
export function addClientEvent(data) {
  return request({
    url: '/system/clientEvent',
    method: 'post',
    data: data
  })
}

// 修改客户事件
export function updateClientEvent(data) {
  return request({
    url: '/system/clientEvent',
    method: 'put',
    data: data
  })
}

// 删除客户事件
export function delClientEvent(id) {
  return request({
    url: '/system/clientEvent/' + id,
    method: 'delete'
  })
}
