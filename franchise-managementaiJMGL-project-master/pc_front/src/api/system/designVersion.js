import request from '@/utils/request'

// 查询设计版本列表
export function listDesignVersion(query) {
  return request({
    url: '/system/designVersion/list',
    method: 'get',
    params: query
  })
}

// 查询设计版本详细
export function getDesignVersion(id) {
  return request({
    url: '/system/designVersion/' + id,
    method: 'get'
  })
}

// 新增设计版本
export function addDesignVersion(data) {
  return request({
    url: '/system/designVersion',
    method: 'post',
    data: data
  })
}

// 修改设计版本
export function updateDesignVersion(data) {
  return request({
    url: '/system/designVersion',
    method: 'put',
    data: data
  })
}

// 删除设计版本
export function delDesignVersion(id) {
  return request({
    url: '/system/designVersion/' + id,
    method: 'delete'
  })
}
