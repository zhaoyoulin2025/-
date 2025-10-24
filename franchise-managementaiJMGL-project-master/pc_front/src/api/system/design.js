import request from '@/utils/request'

// 查询设计管理列表
export function listDesign(query) {
  return request({
    url: '/system/design/list',
    method: 'get',
    params: query
  })
}

// 查询设计管理详细
export function assignDesignTask(params) {
  return request({
    url: '/system/design/assignDesignTask',
    method: 'post',
    data: params
  })
}

export function getDesign(id) {
  return request({
    url: '/system/design/' + id,
    method: 'get'
  })
}

export function addDesignFlowRecord(detail) {
  return request({
    url: '/system/design/addRecord', // 后端接口地址，根据实际路径调整（可能需要完整路径如/system/design/addRecord）
    method: 'post', // 对应后端的@GetMapping
    data: detail // GET请求用params传递参数（若后端要求RequestBody，这里可能需要调整为data）
  })
}

export function submitDesignAudit(data) {
  return request({
    url: '/system/design/submitAudit', // 后端实际接口路径
    method: 'post',
    data: data // 参数放请求体（与后端@RequestBody对应）
  });
}

// 新增设计管理
export function addDesign(data) {
  return request({
    url: '/system/design',
    method: 'post',
    data: data
  })
}

// 修改设计管理
export function updateDesign(data) {
  return request({
    url: '/system/design',
    method: 'put',
    data: data
  })
}

// 删除设计管理
export function delDesign(id) {
  return request({
    url: '/system/design/' + id,
    method: 'delete'
  })
}

export function readMeaasge(id) {
  return request({
    url: '/system/design/readMessage/' + id,
    method: 'get'
  })
}
