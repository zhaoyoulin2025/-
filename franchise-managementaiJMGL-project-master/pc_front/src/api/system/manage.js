import request from '@/utils/request'

// 查询监理详情列表
export function listManage(query) {
  return request({
    url: '/system/manage/list',
    method: 'get',
    params: query
  })
}

export function listManageshop(query) {
  return request({
    url: '/system/manage/listshop',
    method: 'get',
    params: query
  })
}

// 查询监理详情详细
export function getManage(id) {
  return request({
    url: '/system/manage/' + id,
    method: 'get'
  })
}

// 新增监理详情
export function addManage(data) {
  return request({
    url: '/system/manage',
    method: 'post',
    data: data
  })
}

// 修改监理详情
export function updateManage(data) {
  return request({
    url: '/system/manage',
    method: 'put',
    data: data
  })
}

// 删除监理详情
export function delManage(id) {
  return request({
    url: '/system/manage/' + id,
    method: 'delete'
  })
}
