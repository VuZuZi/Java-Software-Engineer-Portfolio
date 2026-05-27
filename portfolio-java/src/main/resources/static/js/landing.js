// ===== LANDING PAGE SPECIFIC JS =====

// AOS Animation
function initAOS() {
    if (typeof AOS !== 'undefined') {
        AOS.init({
            duration: 1000,
            once: true
        });
    }
}

// Typed.js
function initTyped() {
    if (typeof Typed !== 'undefined') {
        var typed = new Typed('#typed-text', {
            strings: ['Đoàn Đình Vũ', 'Java Developer', 'Spring Boot Expert', 'Problem Solver'],
            typeSpeed: 50,
            backSpeed: 30,
            loop: true
        });
    }
}

// Initialize
document.addEventListener('DOMContentLoaded', function() {
    initAOS();
    initTyped();
});