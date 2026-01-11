// 工具函数

// 格式化日期时间
function formatDateTime(dateString) {
    if (!dateString) return '-';
    const date = new Date(dateString);
    return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
    });
}

// 格式化日期
function formatDate(dateString) {
    if (!dateString) return '-';
    const date = new Date(dateString);
    return date.toLocaleDateString('zh-CN');
}

// 显示成功提示
function showSuccess(message) {
    showAlert(message, 'success');
}

// 显示错误提示
function showError(message) {
    showAlert(message, 'danger');
}

// 显示警告提示
function showWarning(message) {
    showAlert(message, 'warning');
}

// 显示提示消息
function showAlert(message, type = 'info') {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show position-fixed top-0 start-50 translate-middle-x mt-3`;
    alertDiv.style.zIndex = '9999';
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    document.body.appendChild(alertDiv);

    setTimeout(() => {
        alertDiv.remove();
    }, 3000);
}

// 显示加载动画
function showLoading(element) {
    const spinner = document.createElement('div');
    spinner.className = 'text-center my-5';
    spinner.innerHTML = `
        <div class="spinner-border text-primary" role="status">
            <span class="visually-hidden">加载中...</span>
        </div>
        <p class="mt-2">加载中...</p>
    `;
    element.innerHTML = '';
    element.appendChild(spinner);
}

// 确认对话框
function confirmAction(message) {
    return confirm(message);
}

// 获取票务状态徽章
function getTicketStatusBadge(status) {
    const statusMap = {
        'RESERVED': '<span class="badge bg-warning">已预订</span>',
        'CONFIRMED': '<span class="badge bg-success">已确认</span>',
        'CANCELLED': '<span class="badge bg-danger">已取消</span>',
        'USED': '<span class="badge bg-secondary">已使用</span>'
    };
    return statusMap[status] || '<span class="badge bg-secondary">未知</span>';
}

// 获取支付状态徽章
function getPaymentStatusBadge(status) {
    const statusMap = {
        'PENDING': '<span class="badge bg-warning">待支付</span>',
        'COMPLETED': '<span class="badge bg-success">已完成</span>',
        'FAILED': '<span class="badge bg-danger">失败</span>',
        'REFUNDED': '<span class="badge bg-info">已退款</span>'
    };
    return statusMap[status] || '<span class="badge bg-secondary">未知</span>';
}

// 获取支付方式文本
function getPaymentMethodText(method) {
    const methodMap = {
        'ALIPAY': '支付宝',
        'WECHAT': '微信支付',
        'CREDIT_CARD': '信用卡',
        'DEBIT_CARD': '借记卡'
    };
    return methodMap[method] || method;
}

// 表单验证
function validateForm(formId) {
    const form = document.getElementById(formId);
    if (!form.checkValidity()) {
        form.classList.add('was-validated');
        return false;
    }
    return true;
}

// 重置表单
function resetForm(formId) {
    const form = document.getElementById(formId);
    form.reset();
    form.classList.remove('was-validated');
}

// 格式化金额
function formatCurrency(amount) {
    return '¥' + (amount || 0).toFixed(2);
}

// 生成唯一ID
function generateId() {
    return Date.now().toString(36) + Math.random().toString(36).substr(2);
}

// 防抖函数
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}
