# Movie Ticket System - API Documentation

**项目名称**: movie-ticket-system
**版本**: v1.0.0
**基础URL**: http://localhost:8090 (通过API Gateway访问)

---

## 目录

- [1. User Service API](#1-user-service-api)
- [2. Movie Service API](#2-movie-service-api)
- [3. Ticket Service API](#3-ticket-service-api)
- [4. Payment Service API](#4-payment-service-api)
- [5. Recommendation Service API](#5-recommendation-service-api)
- [6. 通用响应格式](#6-通用响应格式)
- [7. 错误码说明](#7-错误码说明)

---

## 1. User Service API

**服务端口**: 8081
**路由前缀**: /api/users, /api/auth

### 1.1 用户认证

#### 1.1.1 用户注册

**请求:**
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "email": "test@example.com",
  "password": "123456",
  "name": "张三",
  "phone": "13800138000"
}
```

**响应:**
```json
{
  "port": "8081",
  "hostname": "user-service",
  "status": "SUCCESS",
  "data": {
    "id": "1",
    "username": "testuser",
    "email": "test@example.com",
    "name": "张三",
    "phone": "13800138000",
    "totalSpent": 0.0,
    "ticketsPurchased": 0,
    "createdAt": "2025-12-30T10:30:00"
  }
}
```

#### 1.1.2 用户登录

**请求:**
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "123456"
}
```

**响应:**
```json
{
  "port": "8081",
  "hostname": "user-service",
  "status": "SUCCESS",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0dXNlciIsImlhdCI6MTczNTUzODQwMCwiZXhwIjoxNzM1NjI0ODAwfQ.abc123...",
    "userId": "1",
    "username": "testuser",
    "name": "张三",
    "email": "test@example.com"
  }
}
```

**Token有效期**: 24小时（86400000毫秒）

---

### 1.2 用户管理

#### 1.2.1 获取所有用户

**请求:**
```http
GET /api/users
```

**响应:**
```json
{
  "port": "8081",
  "hostname": "user-service",
  "status": "SUCCESS",
  "data": [
    {
      "id": "1",
      "username": "testuser",
      "email": "test@example.com",
      "name": "张三",
      "phone": "13800138000",
      "totalSpent": 90.0,
      "ticketsPurchased": 2,
      "createdAt": "2025-12-30T10:30:00"
    }
  ],
  "count": 1
}
```

#### 1.2.2 获取单个用户

**请求:**
```http
GET /api/users/{id}
```

**响应:**
```json
{
  "port": "8081",
  "hostname": "user-service",
  "status": "SUCCESS",
  "data": {
    "id": "1",
    "username": "testuser",
    "email": "test@example.com",
    "name": "张三",
    "phone": "13800138000",
    "totalSpent": 90.0,
    "ticketsPurchased": 2,
    "createdAt": "2025-12-30T10:30:00"
  }
}
```

#### 1.2.3 按用户名查询

**请求:**
```http
GET /api/users/username/{username}
```

**响应:** 同1.2.2

#### 1.2.4 更新用户信息

**请求:**
```http
PUT /api/users/{id}
Content-Type: application/json

{
  "username": "testuser",
  "email": "newemail@example.com",
  "password": "newpassword",
  "name": "张三丰",
  "phone": "13900139000"
}
```

**响应:** 同1.2.2

#### 1.2.5 删除用户

**请求:**
```http
DELETE /api/users/{id}
```

**响应:**
```
HTTP/1.1 204 No Content
```

---

### 1.3 购票历史

#### 1.3.1 添加购票历史

**请求:**
```http
POST /api/users/{userId}/purchase
Content-Type: application/json

{
  "ticketId": "1",
  "movieTitle": "流浪地球3",
  "showtime": "2025-12-30T14:00:00",
  "seatNumber": "A5",
  "price": 45.0,
  "paymentId": "100"
}
```

**响应:**
```json
{
  "port": "8081",
  "hostname": "user-service",
  "status": "SUCCESS",
  "data": {
    "id": "1",
    "userId": "1",
    "ticketId": "1",
    "movieTitle": "流浪地球3",
    "showtime": "2025-12-30T14:00:00",
    "seatNumber": "A5",
    "price": 45.0,
    "paymentId": "100",
    "purchasedAt": "2025-12-30T12:00:00"
  }
}
```

#### 1.3.2 获取用户购票历史

**请求:**
```http
GET /api/users/{userId}/history
```

**响应:**
```json
{
  "port": "8081",
  "hostname": "user-service",
  "status": "SUCCESS",
  "data": [
    {
      "id": "1",
      "userId": "1",
      "ticketId": "1",
      "movieTitle": "流浪地球3",
      "showtime": "2025-12-30T14:00:00",
      "seatNumber": "A5",
      "price": 45.0,
      "paymentId": "100",
      "purchasedAt": "2025-12-30T12:00:00"
    }
  ],
  "count": 1
}
```

#### 1.3.3 健康检查

**请求:**
```http
GET /api/health
```

**响应:**
```json
{
  "status": "UP",
  "port": "8081",
  "hostname": "user-service",
  "service": "user-service",
  "timestamp": 1735538400000
}
```

---

## 2. Movie Service API

**服务端口**: 8082
**路由前缀**: /api/movies, /api/showtimes

### 2.1 电影管理

#### 2.1.1 创建电影

**请求:**
```http
POST /api/movies
Content-Type: application/json

{
  "title": "流浪地球3",
  "genre": "科幻",
  "director": "郭帆",
  "duration": 180,
  "rating": 8.5,
  "description": "太阳即将毁灭，人类启动流浪地球计划...",
  "releaseDate": "2025-12-30",
  "posterUrl": "https://example.com/poster.jpg",
  "showing": true
}
```

**响应:**
```json
{
  "port": "8082",
  "hostname": "movie-service",
  "status": "SUCCESS",
  "data": {
    "id": "1",
    "title": "流浪地球3",
    "genre": "科幻",
    "director": "郭帆",
    "duration": 180,
    "rating": 8.5,
    "description": "太阳即将毁灭，人类启动流浪地球计划...",
    "releaseDate": "2025-12-30",
    "posterUrl": "https://example.com/poster.jpg",
    "showing": true,
    "createdAt": "2025-12-30T10:00:00"
  }
}
```

#### 2.1.2 获取所有电影

**请求:**
```http
GET /api/movies?showing=true
```

**参数:**
- `showing` (可选): true/false，是否只显示正在放映的电影

**响应:**
```json
{
  "port": "8082",
  "hostname": "movie-service",
  "status": "SUCCESS",
  "data": [
    {
      "id": "1",
      "title": "流浪地球3",
      "genre": "科幻",
      "director": "郭帆",
      "duration": 180,
      "rating": 8.5,
      "showing": true
    }
  ],
  "count": 1
}
```

#### 2.1.3 获取单个电影

**请求:**
```http
GET /api/movies/{id}
```

**响应:** 同2.1.1

#### 2.1.4 搜索电影

**请求:**
```http
GET /api/movies/search?query=流浪地球
```

**参数:**
- `query` (必填): 搜索关键词（匹配标题、导演、描述）

**响应:** 同2.1.2

#### 2.1.5 按类型查询电影

**请求:**
```http
GET /api/movies/genre/{genre}
```

**示例:**
```http
GET /api/movies/genre/科幻
```

**响应:** 同2.1.2

#### 2.1.6 更新电影

**请求:**
```http
PUT /api/movies/{id}
Content-Type: application/json

{
  "title": "流浪地球3",
  "genre": "科幻",
  "rating": 9.0,
  ...
}
```

**响应:** 同2.1.1

#### 2.1.7 删除电影

**请求:**
```http
DELETE /api/movies/{id}
```

**响应:**
```
HTTP/1.1 204 No Content
```

---

### 2.2 排片管理

#### 2.2.1 创建排片

**请求:**
```http
POST /api/showtimes
Content-Type: application/json

{
  "movieId": "1",
  "startTime": "2025-12-30T14:00:00",
  "endTime": "2025-12-30T17:00:00",
  "hall": "1号厅",
  "price": 45.0,
  "totalSeats": 100,
  "availableSeats": 100
}
```

**响应:**
```json
{
  "port": "8082",
  "hostname": "movie-service",
  "status": "SUCCESS",
  "data": {
    "id": "1",
    "movieId": "1",
    "startTime": "2025-12-30T14:00:00",
    "endTime": "2025-12-30T17:00:00",
    "hall": "1号厅",
    "price": 45.0,
    "totalSeats": 100,
    "availableSeats": 100,
    "createdAt": "2025-12-30T10:00:00"
  }
}
```

#### 2.2.2 获取所有排片

**请求:**
```http
GET /api/showtimes
```

**响应:** 返回所有排片列表

#### 2.2.3 获取电影的所有排片

**请求:**
```http
GET /api/showtimes/movie/{movieId}
```

**响应:**
```json
{
  "port": "8082",
  "hostname": "movie-service",
  "status": "SUCCESS",
  "data": [
    {
      "id": "1",
      "movieId": "1",
      "startTime": "2025-12-30T14:00:00",
      "hall": "1号厅",
      "availableSeats": 95
    }
  ],
  "count": 1
}
```

#### 2.2.4 获取单个排片

**请求:**
```http
GET /api/showtimes/{id}
```

**响应:** 同2.2.1

#### 2.2.5 预留座位

**请求:**
```http
POST /api/showtimes/{id}/reserve?seats=1
```

**参数:**
- `seats` (必填): 预留座位数量

**响应:**
```json
{
  "port": "8082",
  "hostname": "movie-service",
  "status": "SUCCESS",
  "message": "座位预留成功",
  "data": {
    "availableSeats": 99
  }
}
```

#### 2.2.6 释放座位

**请求:**
```http
POST /api/showtimes/{id}/release?seats=1
```

**响应:**
```json
{
  "port": "8082",
  "hostname": "movie-service",
  "status": "SUCCESS",
  "message": "座位释放成功",
  "data": {
    "availableSeats": 100
  }
}
```

#### 2.2.7 健康检查

**请求:**
```http
GET /api/health
```

**响应:**
```json
{
  "status": "UP",
  "port": "8082",
  "hostname": "movie-service",
  "service": "movie-service",
  "timestamp": 1735538400000
}
```

---

## 3. Ticket Service API

**服务端口**: 8083
**路由前缀**: /api/tickets

**说明**: Ticket Service通过OpenFeign调用User Service和Movie Service

### 3.1 票务管理

#### 3.1.1 预订电影票

**请求:**
```http
POST /api/tickets/book
Content-Type: application/json

{
  "userId": "1",
  "showtimeId": "1",
  "movieId": "1",
  "seatNumber": "A5",
  "price": 45.0
}
```

**业务逻辑:**
1. 调用user-service验证用户存在性
2. 调用movie-service获取排片信息
3. 检查座位是否已被预订
4. 调用movie-service预留座位
5. 创建票务记录（状态=RESERVED）

**响应:**
```json
{
  "port": "8083",
  "hostname": "ticket-service",
  "status": "SUCCESS",
  "data": {
    "id": "1",
    "userId": "1",
    "showtimeId": "1",
    "movieId": "1",
    "seatNumber": "A5",
    "price": 45.0,
    "status": "RESERVED",
    "paymentId": null,
    "bookingTime": "2025-12-30T12:00:00",
    "expiryTime": "2025-12-30T12:15:00",
    "createdAt": "2025-12-30T12:00:00"
  }
}
```

**票务状态:**
- `RESERVED`: 已预订（15分钟内需完成支付）
- `PAID`: 已支付
- `CANCELLED`: 已取消

#### 3.1.2 获取所有票务

**请求:**
```http
GET /api/tickets
```

**响应:**
```json
{
  "port": "8083",
  "hostname": "ticket-service",
  "status": "SUCCESS",
  "data": [
    {
      "id": "1",
      "userId": "1",
      "seatNumber": "A5",
      "status": "PAID"
    }
  ],
  "count": 1
}
```

#### 3.1.3 获取单个票务

**请求:**
```http
GET /api/tickets/{id}
```

**响应:** 同3.1.1

#### 3.1.4 获取用户的所有票务

**请求:**
```http
GET /api/tickets/user/{userId}
```

**响应:** 返回该用户的所有票务记录

#### 3.1.5 获取排片的所有票务

**请求:**
```http
GET /api/tickets/showtime/{showtimeId}
```

**响应:** 返回该排片的所有票务记录

#### 3.1.6 确认支付

**请求:**
```http
POST /api/tickets/{id}/confirm?paymentId=100
```

**参数:**
- `paymentId` (必填): 支付记录ID

**响应:**
```json
{
  "port": "8083",
  "hostname": "ticket-service",
  "status": "SUCCESS",
  "data": {
    "id": "1",
    "status": "PAID",
    "paymentId": "100"
  }
}
```

#### 3.1.7 取消票务

**请求:**
```http
POST /api/tickets/{id}/cancel
```

**响应:**
```json
{
  "port": "8083",
  "hostname": "ticket-service",
  "status": "SUCCESS",
  "message": "Ticket cancelled successfully"
}
```

#### 3.1.8 健康检查

**请求:**
```http
GET /api/health
```

**响应:**
```json
{
  "status": "UP",
  "port": "8083",
  "hostname": "ticket-service",
  "service": "ticket-service",
  "timestamp": 1735538400000
}
```

---

## 4. Payment Service API

**服务端口**: 8084
**路由前缀**: /api/payments, /api/refunds, /config

**说明**: Payment Service通过OpenFeign调用Ticket Service和User Service，并通过RabbitMQ发送异步消息

### 4.1 支付管理

#### 4.1.1 处理支付

**请求:**
```http
POST /api/payments/process
Content-Type: application/json

{
  "userId": "1",
  "ticketId": "1",
  "amount": 45.0,
  "paymentMethod": "WECHAT",
  "ticketDetails": {
    "movieTitle": "流浪地球3",
    "showtime": "2025-12-30T14:00:00",
    "seatNumber": "A5"
  }
}
```

**支付方式 (paymentMethod):**
- `WECHAT`: 微信支付
- `ALIPAY`: 支付宝
- `CREDIT_CARD`: 信用卡

**业务逻辑:**
1. 创建支付记录（状态=COMPLETED）
2. 调用ticket-service确认支付
3. 调用user-service添加购票历史
4. 发送支付成功消息到RabbitMQ

**响应:**
```json
{
  "port": "8084",
  "hostname": "payment-service",
  "status": "SUCCESS",
  "data": {
    "id": "1",
    "userId": "1",
    "ticketId": "1",
    "amount": 45.0,
    "paymentMethod": "WECHAT",
    "status": "COMPLETED",
    "transactionId": "TXN-a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "createdAt": "2025-12-30T12:00:00",
    "processedAt": "2025-12-30T12:00:00"
  }
}
```

**支付状态:**
- `PENDING`: 待支付
- `COMPLETED`: 已完成
- `FAILED`: 失败
- `REFUNDED`: 已退款

#### 4.1.2 获取所有支付记录

**请求:**
```http
GET /api/payments
```

**响应:**
```json
{
  "port": "8084",
  "hostname": "payment-service",
  "status": "SUCCESS",
  "data": [
    {
      "id": "1",
      "userId": "1",
      "amount": 45.0,
      "status": "COMPLETED"
    }
  ],
  "count": 1
}
```

#### 4.1.3 获取单个支付记录

**请求:**
```http
GET /api/payments/{id}
```

**响应:** 同4.1.1

#### 4.1.4 获取用户的支付记录

**请求:**
```http
GET /api/payments/user/{userId}
```

**响应:** 返回该用户的所有支付记录

---

### 4.2 退款管理

#### 4.2.1 申请退款

**请求:**
```http
POST /api/refunds/request
Content-Type: application/json

{
  "paymentId": "1",
  "ticketId": "1",
  "amount": 45.0,
  "reason": "临时有事无法观影"
}
```

**业务逻辑:**
1. 验证支付记录状态为COMPLETED
2. 创建退款记录（状态=APPROVED）
3. 调用ticket-service取消订单
4. 发送退款请求消息到RabbitMQ

**响应:**
```json
{
  "port": "8084",
  "hostname": "payment-service",
  "status": "SUCCESS",
  "data": {
    "id": "1",
    "paymentId": "1",
    "ticketId": "1",
    "amount": 45.0,
    "reason": "临时有事无法观影",
    "status": "APPROVED",
    "createdAt": "2025-12-30T13:00:00",
    "processedAt": null
  }
}
```

**退款状态:**
- `PENDING`: 待审核
- `APPROVED`: 已批准
- `REJECTED`: 已拒绝
- `COMPLETED`: 已完成

#### 4.2.2 处理退款

**请求:**
```http
POST /api/refunds/{id}/process
```

**响应:**
```json
{
  "port": "8084",
  "hostname": "payment-service",
  "status": "SUCCESS",
  "data": {
    "id": "1",
    "status": "COMPLETED",
    "processedAt": "2025-12-30T13:05:00"
  }
}
```

---

### 4.3 配置管理（Nacos Config示例）

#### 4.3.1 获取当前配置

**请求:**
```http
GET /config/current
```

**响应:**
```json
{
  "timeout": 30,
  "refundProcessTime": 15,
  "maxRefundAttempts": 3,
  "notificationEnabled": true,
  "notificationMessage": "支付成功",
  "description": "这些配置可以在Nacos配置中心动态修改并实时生效"
}
```

**说明:** 这些配置值来自Nacos Config，修改Nacos中的配置后会实时刷新（无需重启服务）

#### 4.3.2 健康检查

**请求:**
```http
GET /api/health
```

**响应:**
```json
{
  "status": "UP",
  "port": "8084",
  "hostname": "payment-service",
  "service": "payment-service",
  "timestamp": 1735538400000
}
```

---

## 5. Recommendation Service API

**服务端口**: 8085
**路由前缀**: /api/recommendations

**说明**: Recommendation Service是无状态服务，通过OpenFeign调用Movie Service和User Service

### 5.1 电影推荐

#### 5.1.1 获取用户个性化推荐

**请求:**
```http
GET /api/recommendations/user/{userId}
```

**业务逻辑:**
1. 调用user-service获取用户购票历史
2. 分析用户偏好（类型、导演等）
3. 调用movie-service获取推荐电影
4. 返回最多5部推荐电影

**响应:**
```json
{
  "port": "8085",
  "hostname": "recommendation-service",
  "status": "SUCCESS",
  "data": [
    {
      "id": "2",
      "title": "沙丘2",
      "genre": "科幻",
      "rating": 8.8
    },
    {
      "id": "3",
      "title": "星际穿越",
      "genre": "科幻",
      "rating": 9.3
    }
  ],
  "count": 2
}
```

#### 5.1.2 按类型推荐电影

**请求:**
```http
GET /api/recommendations/genre/{genre}
```

**示例:**
```http
GET /api/recommendations/genre/科幻
```

**响应:**
```json
{
  "port": "8085",
  "hostname": "recommendation-service",
  "status": "SUCCESS",
  "data": [
    {
      "id": "1",
      "title": "流浪地球3",
      "genre": "科幻"
    }
  ],
  "count": 1
}
```

#### 5.1.3 获取热门电影

**请求:**
```http
GET /api/recommendations/popular
```

**业务逻辑:**
1. 调用movie-service获取所有正在放映的电影
2. 按评分（averageRating）降序排序
3. 返回前10部电影

**响应:**
```json
{
  "port": "8085",
  "hostname": "recommendation-service",
  "status": "SUCCESS",
  "data": [
    {
      "id": "5",
      "title": "星际穿越",
      "rating": 9.3
    },
    {
      "id": "2",
      "title": "沙丘2",
      "rating": 8.8
    }
  ],
  "count": 2
}
```

#### 5.1.4 健康检查

**请求:**
```http
GET /api/health
```

**响应:**
```json
{
  "status": "UP",
  "port": "8085",
  "hostname": "recommendation-service",
  "service": "recommendation-service",
  "timestamp": 1735538400000
}
```

---

## 6. 通用响应格式

### 6.1 成功响应

所有成功的API响应都遵循以下格式：

```json
{
  "port": "8081",
  "hostname": "user-service",
  "status": "SUCCESS",
  "data": { ... },
  "count": 10  // 仅列表接口包含
}
```

**字段说明:**
- `port`: 处理请求的服务实例端口
- `hostname`: 处理请求的服务实例主机名
- `status`: 响应状态，成功时为 "SUCCESS"
- `data`: 响应数据（对象或数组）
- `count`: 数据数量（仅列表接口）

### 6.2 错误响应

错误响应格式：

```json
{
  "timestamp": "2025-12-30T12:00:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "User with id 999 not found",
  "path": "/api/users/999"
}
```

或：

```json
{
  "port": "8083",
  "hostname": "ticket-service",
  "status": "ERROR",
  "message": "Seat already booked"
}
```

---

## 7. 错误码说明

### HTTP状态码

| 状态码 | 说明 | 示例场景 |
|--------|------|---------|
| 200 | 成功 | GET请求成功 |
| 201 | 创建成功 | POST创建资源成功 |
| 204 | 无内容 | DELETE删除成功 |
| 400 | 请求错误 | 参数格式错误、缺少必填字段 |
| 401 | 未授权 | 登录失败、Token无效 |
| 404 | 资源不存在 | 用户ID不存在、电影ID不存在 |
| 500 | 服务器错误 | 服务异常、数据库错误 |

### 业务错误

| 错误信息 | 原因 | 解决方案 |
|---------|------|---------|
| "User not found or service unavailable" | user-service不可用或用户不存在 | 检查user-service状态，确认用户ID |
| "Movie service unavailable" | movie-service不可用（触发fallback） | 检查movie-service状态 |
| "Seat already booked" | 座位已被预订 | 选择其他座位 |
| "Ticket is not in RESERVED status" | 票务状态不正确 | 检查票务状态 |
| "Payment is not in COMPLETED status" | 支付状态不正确 | 检查支付记录 |
| "Failed to reserve seat" | 座位预留失败（可能无剩余座位） | 检查座位可用性 |

---

## 8. 服务间调用关系

```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │
       ▼
┌──────────────┐
│   Gateway    │  (8090) 统一入口
└──────┬───────┘
       │
       ├───────────────────────────────────────────┐
       │                                           │
       ▼                                           ▼
┌──────────────┐                          ┌──────────────┐
│ User-Service │                          │Movie-Service │
│    (8081)    │                          │    (8082)    │
└──────┬───────┘                          └──────┬───────┘
       │                                         │
       │  ┌────────────────────────────────────┐ │
       │  │                                    │ │
       ▼  ▼                                    ▼ ▼
┌────────────────┐                    ┌────────────────┐
│Ticket-Service  │ ──Feign调用───────>│Movie-Service   │
│    (8083)      │ <──响应────────────│    (8082)      │
└────────┬───────┘                    └────────────────┘
         │
         │  ┌──Feign调用────>┌──────────────┐
         │  │                │User-Service  │
         ▼  ▼                │    (8081)    │
┌────────────────┐           └──────────────┘
│Payment-Service │
│    (8084)      │ ──RabbitMQ消息───┐
└────────────────┘                  │
                                    ▼
                          ┌──────────────────┐
                          │   RabbitMQ       │
                          │  (5672/15672)    │
                          └──────┬───────────┘
                                 │
                        消费消息 │
                                 ▼
                        ┌──────────────┐
                        │User-Service  │
                        │    (8081)    │
                        └──────────────┘

┌───────────────────────┐
│Recommendation-Service │ ──Feign调用───>┌──────────────┐
│       (8085)          │                │User-Service  │
│                       │                │Movie-Service │
└───────────────────────┘                └──────────────┘
```

---

## 9. Gateway路由配置

所有API均可通过Gateway统一入口访问：

| 路径前缀 | 目标服务 | 服务端口 |
|---------|---------|---------|
| `/api/users/**`, `/api/auth/**` | user-service | 8081 |
| `/api/movies/**`, `/api/showtimes/**` | movie-service | 8082 |
| `/api/tickets/**` | ticket-service | 8083 |
| `/api/payments/**`, `/api/refunds/**`, `/config/**` | payment-service | 8084 |
| `/api/recommendations/**` | recommendation-service | 8085 |

**推荐访问方式:** 通过Gateway统一入口 `http://localhost:8090/api/*`

---

## 10. 使用示例

### 完整购票流程

```bash
# 1. 用户注册
curl -X POST http://localhost:8090/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "demo_user",
    "email": "demo@example.com",
    "password": "123456",
    "name": "演示用户",
    "phone": "13800000001"
  }'

# 2. 用户登录
curl -X POST http://localhost:8090/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "demo_user",
    "password": "123456"
  }'

# 3. 浏览电影
curl http://localhost:8090/api/movies?showing=true

# 4. 查看电影排片
curl http://localhost:8090/api/showtimes/movie/1

# 5. 预订电影票
curl -X POST http://localhost:8090/api/tickets/book \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "1",
    "showtimeId": "1",
    "movieId": "1",
    "seatNumber": "A5",
    "price": 45.0
  }'

# 6. 处理支付
curl -X POST http://localhost:8090/api/payments/process \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "1",
    "ticketId": "1",
    "amount": 45.0,
    "paymentMethod": "WECHAT",
    "ticketDetails": {
      "movieTitle": "流浪地球3",
      "showtime": "2025-12-30T14:00:00",
      "seatNumber": "A5"
    }
  }'

# 7. 查看购票历史
curl http://localhost:8090/api/users/1/history

# 8. 获取个性化推荐
curl http://localhost:8090/api/recommendations/user/1
```

---

## 11. 服务健康检查汇总

| 服务 | 健康检查端点 | 端口 |
|------|------------|------|
| user-service | http://localhost:8081/api/health | 8081 |
| movie-service | http://localhost:8082/api/health | 8082 |
| ticket-service | http://localhost:8083/api/health | 8083 |
| payment-service | http://localhost:8084/api/health | 8084 |
| recommendation-service | http://localhost:8085/api/health | 8085 |
| gateway-service | http://localhost:8090/actuator/health | 8090 |

---

## 12. Nacos服务发现

查询已注册服务实例：

```bash
# 查询user-service实例
curl "http://localhost:8848/nacos/v1/ns/instance/list?serviceName=user-service&groupName=CINEMA_GROUP&namespaceId=cinema"

# 查询所有服务
curl "http://localhost:8848/nacos/v1/ns/service/list?pageNo=1&pageSize=10&groupName=CINEMA_GROUP&namespaceId=cinema"
```

---

## 13. RabbitMQ管理

**RabbitMQ管理界面:** http://localhost:15672
**账号/密码:** cinema/cinema123

**队列列表:**
- `cinema.payment.success.queue`: 支付成功队列
- `cinema.refund.queue`: 退款队列

**Exchange:**
- `cinema.payment.exchange` (type: topic)

**Routing Keys:**
- `payment.success`: 支付成功消息
- `payment.refund`: 退款消息

---

**文档版本**: v1.0.0
**最后更新**: 2025-12-30
**维护者**: movie-ticket-system团队
