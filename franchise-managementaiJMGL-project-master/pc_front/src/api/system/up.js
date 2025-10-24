import request from '@/utils/request'

// 查询客户跟进记录列表
export function listUp(query) {
  return request({
    url: '/system/up/list',
    method: 'get',
    params: query
  })
}

// 查询客户跟进记录详细
export function getUp(id) {
  return request({
    url: '/system/up/' + id,
    method: 'get'
  })
}

// 新增客户跟进记录
export function addUp(data) {
  return request({
    url: '/system/up',
    method: 'post',
    data: data
  })
}

// 修改客户跟进记录
export function updateUp(data) {
  return request({
    url: '/system/up',
    method: 'put',
    data: data
  })
}

// 删除客户跟进记录
export function delUp(id) {
  return request({
    url: '/system/up/' + id,
    method: 'delete'
  })
}
