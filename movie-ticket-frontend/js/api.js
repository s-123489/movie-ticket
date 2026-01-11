// API 基础配置
// 在 Docker 环境中，使用 nginx 反向代理到 /api
// 在开发环境中，直接访问 localhost:8090
const API_BASE_URL = window.location.hostname === 'localhost' && window.location.port === '8000'
    ? 'http://localhost:8090/api'  // 开发环境
    : '/api';  // Docker/生产环境，使用 nginx 代理

// API 工具类
const API = {
    // 认证服务
    auth: {
        login: (credentials) => fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(credentials)
        }).then(res => res.json()),
        register: (user) => fetch(`${API_BASE_URL}/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(user)
        }).then(res => res.json())
    },

    // 用户服务
    users: {
        getAll: () => fetch(`${API_BASE_URL}/users`).then(res => res.json()),
        getById: (id) => fetch(`${API_BASE_URL}/users/${id}`).then(res => res.json()),
        getByUsername: (username) => fetch(`${API_BASE_URL}/users/username/${username}`).then(res => res.json()),
        create: (user) => fetch(`${API_BASE_URL}/users`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(user)
        }).then(res => res.json()),
        update: (id, user) => fetch(`${API_BASE_URL}/users/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(user)
        }).then(res => res.json()),
        delete: (id) => fetch(`${API_BASE_URL}/users/${id}`, { method: 'DELETE' }),
        getHistory: (userId) => fetch(`${API_BASE_URL}/users/${userId}/history`).then(res => res.json())
    },

    // 电影服务
    movies: {
        getAll: (showing) => {
            const url = showing ? `${API_BASE_URL}/movies?showing=${showing}` : `${API_BASE_URL}/movies`;
            return fetch(url).then(res => res.json());
        },
        getById: (id) => fetch(`${API_BASE_URL}/movies/${id}`).then(res => res.json()),
        search: (query) => fetch(`${API_BASE_URL}/movies/search?query=${encodeURIComponent(query)}`).then(res => res.json()),
        getByGenre: (genre) => fetch(`${API_BASE_URL}/movies/genre/${genre}`).then(res => res.json()),
        create: (movie) => fetch(`${API_BASE_URL}/movies`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(movie)
        }).then(res => res.json()),
        update: (id, movie) => fetch(`${API_BASE_URL}/movies/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(movie)
        }).then(res => res.json()),
        delete: (id) => fetch(`${API_BASE_URL}/movies/${id}`, { method: 'DELETE' })
    },

    // 排片服务
    showtimes: {
        getAll: () => fetch(`${API_BASE_URL}/showtimes`).then(res => res.json()),
        getById: (id) => fetch(`${API_BASE_URL}/showtimes/${id}`).then(res => res.json()),
        getByMovie: (movieId) => fetch(`${API_BASE_URL}/showtimes/movie/${movieId}`).then(res => res.json()),
        create: (showtime) => fetch(`${API_BASE_URL}/showtimes`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(showtime)
        }).then(res => res.json()),
        reserve: (id, seats) => fetch(`${API_BASE_URL}/showtimes/${id}/reserve?seats=${seats}`, {
            method: 'POST'
        }).then(res => res.json()),
        release: (id, seats) => fetch(`${API_BASE_URL}/showtimes/${id}/release?seats=${seats}`, {
            method: 'POST'
        }).then(res => res.json())
    },

    // 票务服务
    tickets: {
        getAll: () => fetch(`${API_BASE_URL}/tickets`).then(res => res.json()),
        getById: (id) => fetch(`${API_BASE_URL}/tickets/${id}`).then(res => res.json()),
        getByUser: (userId) => fetch(`${API_BASE_URL}/tickets/user/${userId}`).then(res => res.json()),
        getByShowtime: (showtimeId) => fetch(`${API_BASE_URL}/tickets/showtime/${showtimeId}`).then(res => res.json()),
        book: (ticket) => fetch(`${API_BASE_URL}/tickets/book`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(ticket)
        }).then(res => res.json()),
        confirm: (id, paymentId) => fetch(`${API_BASE_URL}/tickets/${id}/confirm?paymentId=${paymentId}`, {
            method: 'POST'
        }).then(res => res.json()),
        cancel: (id) => fetch(`${API_BASE_URL}/tickets/${id}/cancel`, {
            method: 'POST'
        }).then(res => res.json())
    },

    // 支付服务
    payments: {
        getAll: () => fetch(`${API_BASE_URL}/payments`).then(res => res.json()),
        getById: (id) => fetch(`${API_BASE_URL}/payments/${id}`).then(res => res.json()),
        getByUser: (userId) => fetch(`${API_BASE_URL}/payments/user/${userId}`).then(res => res.json()),
        process: (payment) => fetch(`${API_BASE_URL}/payments/process`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payment)
        }).then(res => res.json())
    },

    // 退款服务
    refunds: {
        request: (refund) => fetch(`${API_BASE_URL}/refunds/request`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(refund)
        }).then(res => res.json()),
        process: (id) => fetch(`${API_BASE_URL}/refunds/${id}/process`, {
            method: 'POST'
        }).then(res => res.json())
    },

    // 推荐服务
    recommendations: {
        getPopular: () => fetch(`${API_BASE_URL}/recommendations/popular`).then(res => res.json()),
        getPersonalized: (userId) => fetch(`${API_BASE_URL}/recommendations/user/${userId}`).then(res => res.json()),
        getByGenre: (genre) => fetch(`${API_BASE_URL}/recommendations/genre/${genre}`).then(res => res.json())
    }
};
