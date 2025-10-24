import request from '@/utils/request'

// 查询提问者列表
export function listQuestiner(query) {
  return request({
    url: '/system/questiner/list',
    method: 'get',
    params: query
  })
}

// 查询提问者详细
export function getQuestiner(id) {
  return request({
    url: '/system/questiner/' + id,
    method: 'get'
  })
}

// 新增提问者
export function addQuestiner(data) {
  return request({
    url: '/system/questiner',
    method: 'post',
    data: data
  })
}

// 修改提问者
export function updateQuestiner(data) {
  return request({
    url: '/system/questiner',
    method: 'put',
    data: data
  })
}

// 删除提问者
export function delQuestiner(id) {
  return request({
    url: '/system/questiner/' + id,
    method: 'delete'
  })
}
