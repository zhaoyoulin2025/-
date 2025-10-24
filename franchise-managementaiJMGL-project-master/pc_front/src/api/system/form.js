import request from '@/utils/request'

// 查询加盟投资意向列表
export function listForm(query) {
  return request({
    url: '/system/form/list',
    method: 'get',
    params: query
  })
}

// 查询加盟投资意向详细
export function getForm(id) {
  return request({
    url: '/system/form/' + id,
    method: 'get'
  })
}

// 新增加盟投资意向
export function addForm(data) {
  return request({
    url: '/system/form',
    method: 'post',
    data: data
  })
}

// 修改加盟投资意向
export function updateForm(data) {
  return request({
    url: '/system/form',
    method: 'put',
    data: data
  })
}

// 删除加盟投资意向
export function delForm(id) {
  return request({
    url: '/system/form/' + id,
    method: 'delete'
  })
}
