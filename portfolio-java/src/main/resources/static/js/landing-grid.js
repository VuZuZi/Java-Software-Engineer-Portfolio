/* ===== GRID LAYOUT - MENU & INTERACTION ===== */

document.addEventListener('DOMContentLoaded', function () {
    // Initialize AOS
    if (typeof AOS !== 'undefined') {
        AOS.init({ duration: 1000, once: true });
    }

    // Initialize Typed.js
    if (document.getElementById('typed-text')) {
        new Typed('#typed-text', {
            strings: ['Đoàn Đình Vũ', 'Java Developer', 'Spring Boot Expert', 'Problem Solver'],
            typeSpeed: 50,
            backSpeed: 30,
            loop: true
        });
    }

    // ===== MENU & GRID FUNCTIONALITY =====
    const header = document.querySelector('.grid-header');
    const menu = document.querySelector('.grid-menu');
    const menuToggle = document.getElementById('menuToggle');
    const menuLinks = document.querySelectorAll('.grid-menu a');
    const sections = document.querySelectorAll('section[id]');

    // Menu toggle functionality
    if (menuToggle) {
        menuToggle.addEventListener('click', function () {
            menu.classList.toggle('active');
            menuToggle.classList.toggle('active');
        });
    }

    // Close menu when clicking a link
    menuLinks.forEach(link => {
        link.addEventListener('click', function (e) {
            e.preventDefault();
            const targetId = this.getAttribute('href');
            const target = document.querySelector(targetId);

            if (target) {
                // Close mobile menu
                if (menu.classList.contains('active')) {
                    menu.classList.remove('active');
                    if (menuToggle) menuToggle.classList.remove('active');
                }

                // Smooth scroll
                const offset = 100;
                const targetPosition = target.getBoundingClientRect().top + window.pageYOffset - offset;
                window.scrollTo({
                    top: targetPosition,
                    behavior: 'smooth'
                });

                // Update active menu
                updateActiveMenu(targetId);

                // Update URL
                history.pushState(null, null, targetId);
            }
        });
    });

    // Update active menu based on scroll position
    function updateActiveMenu(targetId) {
        menuLinks.forEach(link => {
            link.classList.remove('active');
            if (link.getAttribute('href') === targetId) {
                link.classList.add('active');
            }
        });
    }

    // Scroll event for header shadow and menu highlight
    window.addEventListener('scroll', function () {
        // Header scroll effect
        if (window.scrollY > 50) {
            header.classList.add('scrolled');
        } else {
            header.classList.remove('scrolled');
        }

        // Update active menu based on visible section
        updateActiveMenuOnScroll();
    });

    // Update active menu based on scroll position
    function updateActiveMenuOnScroll() {
        let currentSection = '';
        const scrollPosition = window.scrollY + 150;

        sections.forEach(section => {
            const sectionTop = section.offsetTop;
            const sectionHeight = section.clientHeight;
            if (scrollPosition >= sectionTop && scrollPosition < sectionTop + sectionHeight) {
                currentSection = section.getAttribute('id');
            }
        });

        menuLinks.forEach(link => {
            link.classList.remove('active');
            if (link.getAttribute('href') === '#' + currentSection) {
                link.classList.add('active');
            }
        });
    }

    // Handle hash in URL on page load
    window.addEventListener('load', function () {
        if (window.location.hash) {
            const hash = window.location.hash;
            const target = document.querySelector(hash);
            if (target) {
                setTimeout(() => {
                    const offset = 100;
                    const targetPosition = target.getBoundingClientRect().top + window.pageYOffset - offset;
                    window.scrollTo({
                        top: targetPosition,
                        behavior: 'smooth'
                    });
                    updateActiveMenu(hash);
                }, 100);
            }
        }
    });

    // Smooth scroll for other anchor links (not menu)
    document.querySelectorAll('a[href^="#"]').forEach(link => {
        if (!link.closest('.grid-menu')) {
            link.addEventListener('click', function (e) {
                const targetId = this.getAttribute('href');
                if (targetId === '#') return;

                const target = document.querySelector(targetId);
                if (target) {
                    e.preventDefault();
                    const offset = 100;
                    const targetPosition = target.getBoundingClientRect().top + window.pageYOffset - offset;
                    window.scrollTo({
                        top: targetPosition,
                        behavior: 'smooth'
                    });
                }
            });
        }
    });

    // Close menu when clicking outside (mobile)
    document.addEventListener('click', function (e) {
        if (menu && menu.classList.contains('active')) {
            if (!menu.contains(e.target) && !menuToggle.contains(e.target)) {
                menu.classList.remove('active');
                if (menuToggle) menuToggle.classList.remove('active');
            }
        }
    });

    // Initial active menu check
    updateActiveMenuOnScroll();
});
