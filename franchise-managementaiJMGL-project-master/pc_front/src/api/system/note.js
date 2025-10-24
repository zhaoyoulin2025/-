import request from '@/utils/request'

// 查询商圈跟进笔记（含打卡）列表
export function listNote(query) {
  return request({
    url: '/system/note/list',
    method: 'get',
    params: query
  })
}

// 查询商圈跟进笔记（含打卡）详细
export function getNote(id) {
  return request({
    url: '/system/note/' + id,
    method: 'get'
  })
}

// 新增商圈跟进笔记（含打卡）
export function addNote(data) {
  return request({
    url: '/system/note',
    method: 'post',
    data: data
  })
}

// 修改商圈跟进笔记（含打卡）
export function updateNote(data) {
  return request({
    url: '/system/note',
    method: 'put',
    data: data
  })
}


export function batchAuditNote(data) {
  return request({
    url: '/system/note/batchAudit',
    method: 'post',
    data: data
  })
}

// 删除商圈跟进笔记（含打卡）
export function delNote(id) {
  return request({
    url: '/system/note/' + id,
    method: 'delete'
  })
}
