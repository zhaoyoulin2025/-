import request from '@/utils/request'

// 查询测量信息列表
export function listMeasure(query) {
  return request({
    url: '/system/measure/list',
    method: 'get',
    params: query
  })
}

// 查询测量信息详细
export function getMeasure(id) {
  return request({
    url: '/system/measure/' + id,
    method: 'get'
  })
}


// 新增测量信息
export function addMeasure(data) {
  return request({
    url: '/system/measure',
    method: 'post',
    data: data
  })
}

// 修改测量信息
export function updateMeasure(data) {
  return request({
    url: '/system/measure',
    method: 'put',
    data: data
  })
}

// 删除测量信息
export function delMeasure(id) {
  return request({
    url: '/system/measure/' + id,
    method: 'delete'
  })
}
