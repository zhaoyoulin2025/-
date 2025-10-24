import request from '@/utils/request'

// 查询图纸信息列表
export function listDrawings(query) {
  return request({
    url: '/system/drawings/list',
    method: 'get',
    params: query
  })
}

// 查询图纸信息详细
export function getDrawings(id) {
  return request({
    url: '/system/drawings/' + id,
    method: 'get'
  })
}

export function getDrawingByDesgin(id) {
  return request({
    url: '/system/drawings/desgin/' + id,
    method: 'get'
  })
}

// 新增图纸信息
export function addDrawings(data) {
  return request({
    url: '/system/drawings',
    method: 'post',
    data: data
  })
}

// 修改图纸信息
export function updateDrawings(data) {
  return request({
    url: '/system/drawings',
    method: 'put',
    data: data
  })
}

// 删除图纸信息
export function delDrawings(id) {
  return request({
    url: '/system/drawings/' + id,
    method: 'delete'
  })
}
