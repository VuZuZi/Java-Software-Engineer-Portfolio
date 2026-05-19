/* ===== GRID LAYOUT - MENU, INTERACTION & LANGUAGE ===== */

document.addEventListener('DOMContentLoaded', function () {
    const translations = {
        en: {
            'language.button': 'VI',
            'language.aria': 'Switch to Vietnamese',
            'sidebar.profile': 'Profile',
            'sidebar.jobTitle': 'Java Developer',
            'sidebar.menu': 'Menu',
            'header.brand': 'Portfolio Vu.DD',
            'auth.login': 'Login',
            'auth.logout': 'Logout',
            'role.admin.vn': 'Administrator',
            'role.member': 'Member',
            'role.admin': 'Admin',
            'profile.gender.label': 'Gender:',
            'profile.gender.value': 'Male',
            'profile.age.label': 'Age:',
            'nav.introduction': 'Introduction',
            'nav.service': 'Service',
            'nav.project': 'Project',
            'nav.skills': 'Skills',
            'nav.contact': 'Contact',
            'nav.messages': 'Messages',
            'nav.admin': 'Admin',
            'hero.badge': 'Open for opportunities',
            'hero.headingPrefix': 'Hello, I am a',
            'hero.lead': 'Java Developer with over <strong>2 years of experience</strong>, having worked on two projects at <strong>FPT Software</strong>.',
            'hero.education': 'I have a strong educational background from <strong>CodeGym</strong> and <strong>FPT University</strong>, with solid knowledge of object-oriented programming and the Java ecosystem (<strong>Spring Boot, RESTful APIs</strong>).',
            'hero.fullstack': 'In addition, I have completed multiple projects using <strong>Node.js</strong>, giving me a basic full-stack capability. I am familiar with software development processes and experienced in working within <strong>Agile</strong> and <strong>Scrum</strong> environments.',
            'hero.viewProjects': 'View projects',
            'hero.contact': 'Contact',
            'hero.available': 'Available for work',
            'services.title': 'Services & Expertise',
            'services.subtitle': 'Capabilities I can contribute to your project',
            'services.backend.title': 'Backend Development',
            'services.backend.desc': 'Java, Spring Boot, REST APIs, WebSocket.',
            'services.database.title': 'Database & Data Layer',
            'services.database.desc': 'MySQL, MongoDB, JPA/Hibernate, query optimization.',
            'services.deployment.title': 'Deployment & Collaboration',
            'services.deployment.desc': 'Docker basics, Git/GitHub, Agile/Scrum, production support.',
            'projects.title': 'Featured Projects',
            'projects.subtitle': 'Projects I have contributed to',
            'projects.car.desc': 'Java Developer with over 2 years of experience, having worked on two projects at FPT Software, with a strong educational background from CodeGym and FPT University, Android Framework.',
            'projects.chat.desc': 'Portfolio | WebSocket, STOMP, Redis',
            'projects.portfolio.desc': 'Spring Boot, MySQL, OAuth2, WebSocket',
            'projects.viewAll': 'View all projects',
            'skills.title': 'Skills & Technologies',
            'skills.subtitle': 'Technologies I use in daily development',
            'cta.title': 'Ready for the next project?',
            'cta.subtitle': 'Contact me to discuss collaboration opportunities',
            'cta.button': 'Send message',
            'cta.subjectLabel': 'Subject',
            'cta.subjectPlaceholder': 'Enter a short subject',
            'cta.contentLabel': 'Description',
            'cta.contentPlaceholder': 'Describe your idea, question, or collaboration opportunity...',
            'cta.loginRequired': 'Please log in to send a message to Admin.',
            'cta.validationError': 'Please enter both subject and description.',
            'cta.sending': 'Sending...',
            'cta.success': 'Your message has been sent to Admin.',
            'cta.networkError': 'Connection error. Please try again.',
            'footer.about': 'Introducing my projects, skills, and work experience.',
            'footer.quickLinks': 'Quick links',
            'footer.home': 'Home',
            'footer.projects': 'Projects',
            'footer.skills': 'Skills',
            'footer.contact': 'Contact',
            'footer.connect': 'Connect',
            'footer.location': 'Da Nang, Vietnam',
            'footer.copyright': '&copy; 2025 Portfolio Platform. Built with <i class="fas fa-heart text-danger"></i> by Java Developer',
            title: 'Portfolio - Java Developer',
            typed: ['Doan Dinh Vu', 'Java Developer', 'Spring Boot Developer', 'Problem Solver']
        },
        vi: {
            'language.button': 'EN',
            'language.aria': 'Chuyen sang tieng Anh',
            'sidebar.profile': 'H\u1ed3 s\u01a1',
            'sidebar.jobTitle': 'Java Developer',
            'sidebar.menu': 'Menu',
            'header.brand': 'Portfolio Vu.DD',
            'auth.login': '\u0110\u0103ng nh\u1eadp',
            'auth.logout': '\u0110\u0103ng xu\u1ea5t',
            'role.admin.vn': 'Qu\u1ea3n tr\u1ecb vi\u00ean',
            'role.member': 'Th\u00e0nh vi\u00ean',
            'role.admin': 'Admin',
            'profile.gender.label': 'Gi\u1edbi t\u00ednh:',
            'profile.gender.value': 'Nam',
            'profile.age.label': 'Tu\u1ed5i:',
            'nav.introduction': 'Gi\u1edbi thi\u1ec7u',
            'nav.service': 'D\u1ecbch v\u1ee5',
            'nav.project': 'D\u1ef1 \u00e1n',
            'nav.skills': 'K\u1ef9 n\u0103ng',
            'nav.contact': 'Li\u00ean h\u1ec7',
            'nav.messages': 'Tin nh\u1eafn',
            'nav.admin': 'Admin',
            'hero.badge': '\u0110ang t\u00ecm ki\u1ebfm c\u01a1 h\u1ed9i m\u1edbi',
            'hero.headingPrefix': 'Xin ch\u00e0o, t\u00f4i l\u00e0',
            'hero.lead': 'Java Developer v\u1edbi h\u01a1n <strong>2 n\u0103m kinh nghi\u1ec7m</strong>, \u0111\u00e3 tham gia hai d\u1ef1 \u00e1n t\u1ea1i <strong>FPT Software</strong>.',
            'hero.education': 'T\u00f4i c\u00f3 n\u1ec1n t\u1ea3ng h\u1ecdc t\u1eadp t\u1eeb <strong>CodeGym</strong> v\u00e0 <strong>FPT University</strong>, c\u00f9ng ki\u1ebfn th\u1ee9c v\u1eefng v\u1ec1 l\u1eadp tr\u00ecnh h\u01b0\u1edbng \u0111\u1ed1i t\u01b0\u1ee3ng v\u00e0 h\u1ec7 sinh th\u00e1i Java (<strong>Spring Boot, RESTful APIs</strong>).',
            'hero.fullstack': 'B\u00ean c\u1ea1nh \u0111\u00f3, t\u00f4i \u0111\u00e3 ho\u00e0n th\u00e0nh nhi\u1ec1u d\u1ef1 \u00e1n s\u1eed d\u1ee5ng <strong>Node.js</strong>, gi\u00fap t\u00f4i c\u00f3 n\u0103ng l\u1ef1c full-stack c\u01a1 b\u1ea3n. T\u00f4i quen thu\u1ed9c v\u1edbi quy tr\u00ecnh ph\u00e1t tri\u1ec3n ph\u1ea7n m\u1ec1m v\u00e0 c\u00f3 kinh nghi\u1ec7m l\u00e0m vi\u1ec7c trong m\u00f4i tr\u01b0\u1eddng <strong>Agile</strong> v\u00e0 <strong>Scrum</strong>.',
            'hero.viewProjects': 'Xem d\u1ef1 \u00e1n',
            'hero.contact': 'Li\u00ean h\u1ec7',
            'hero.available': 'S\u1eb5n s\u00e0ng l\u00e0m vi\u1ec7c',
            'services.title': 'D\u1ecbch v\u1ee5 & Chuy\u00ean m\u00f4n',
            'services.subtitle': 'Nh\u1eefng n\u0103ng l\u1ef1c t\u00f4i c\u00f3 th\u1ec3 \u0111\u00f3ng g\u00f3p cho d\u1ef1 \u00e1n',
            'services.backend.title': 'Ph\u00e1t tri\u1ec3n Backend',
            'services.backend.desc': 'Java, Spring Boot, REST APIs, WebSocket.',
            'services.database.title': 'C\u01a1 s\u1edf d\u1eef li\u1ec7u & Data Layer',
            'services.database.desc': 'MySQL, MongoDB, JPA/Hibernate, t\u1ed1i \u01b0u truy v\u1ea5n.',
            'services.deployment.title': 'Tri\u1ec3n khai & C\u1ed9ng t\u00e1c',
            'services.deployment.desc': 'Docker c\u01a1 b\u1ea3n, Git/GitHub, Agile/Scrum, h\u1ed7 tr\u1ee3 production.',
            'projects.title': 'D\u1ef1 \u00e1n n\u1ed5i b\u1eadt',
            'projects.subtitle': 'Nh\u1eefng d\u1ef1 \u00e1n t\u00f4i \u0111\u00e3 tham gia',
            'projects.car.desc': 'Java Developer v\u1edbi h\u01a1n 2 n\u0103m kinh nghi\u1ec7m, \u0111\u00e3 tham gia hai d\u1ef1 \u00e1n t\u1ea1i FPT Software, c\u00f3 n\u1ec1n t\u1ea3ng t\u1eeb CodeGym v\u00e0 FPT University, Android Framework.',
            'projects.chat.desc': 'Portfolio | WebSocket, STOMP, Redis',
            'projects.portfolio.desc': 'Spring Boot, MySQL, OAuth2, WebSocket',
            'projects.viewAll': 'Xem t\u1ea5t c\u1ea3 d\u1ef1 \u00e1n',
            'skills.title': 'K\u1ef9 n\u0103ng & C\u00f4ng ngh\u1ec7',
            'skills.subtitle': 'C\u00e1c c\u00f4ng ngh\u1ec7 t\u00f4i s\u1eed d\u1ee5ng h\u00e0ng ng\u00e0y',
            'cta.title': 'S\u1eb5n s\u00e0ng cho d\u1ef1 \u00e1n ti\u1ebfp theo?',
            'cta.subtitle': 'H\u00e3y li\u00ean h\u1ec7 v\u1edbi t\u00f4i \u0111\u1ec3 c\u00f9ng th\u1ea3o lu\u1eadn v\u1ec1 c\u01a1 h\u1ed9i h\u1ee3p t\u00e1c',
            'cta.button': 'G\u1eedi tin nh\u1eafn',
            'cta.subjectLabel': 'Ti\u00eau \u0111\u1ec1',
            'cta.subjectPlaceholder': 'Nh\u1eadp ti\u00eau \u0111\u1ec1 ng\u1eafn',
            'cta.contentLabel': 'M\u00f4 t\u1ea3',
            'cta.contentPlaceholder': 'M\u00f4 t\u1ea3 \u00fd t\u01b0\u1edfng, c\u00e2u h\u1ecfi ho\u1eb7c c\u01a1 h\u1ed9i h\u1ee3p t\u00e1c...',
            'cta.loginRequired': 'Vui l\u00f2ng \u0111\u0103ng nh\u1eadp \u0111\u1ec3 g\u1eedi tin nh\u1eafn cho Admin.',
            'cta.validationError': 'Vui l\u00f2ng nh\u1eadp \u0111\u1ea7y \u0111\u1ee7 ti\u00eau \u0111\u1ec1 v\u00e0 m\u00f4 t\u1ea3.',
            'cta.sending': '\u0110ang g\u1eedi...',
            'cta.success': 'Tin nh\u1eafn \u0111\u00e3 \u0111\u01b0\u1ee3c g\u1eedi \u0111\u1ebfn Admin.',
            'cta.networkError': 'L\u1ed7i k\u1ebft n\u1ed1i. Vui l\u00f2ng th\u1eed l\u1ea1i.',
            'footer.about': 'Gi\u1edbi thi\u1ec7u d\u1ef1 \u00e1n, k\u1ef9 n\u0103ng v\u00e0 kinh nghi\u1ec7m l\u00e0m vi\u1ec7c c\u1ee7a t\u00f4i.',
            'footer.quickLinks': 'Li\u00ean k\u1ebft nhanh',
            'footer.home': 'Trang ch\u1ee7',
            'footer.projects': 'D\u1ef1 \u00e1n',
            'footer.skills': 'K\u1ef9 n\u0103ng',
            'footer.contact': 'Li\u00ean h\u1ec7',
            'footer.connect': 'K\u1ebft n\u1ed1i',
            'footer.location': '\u0110\u00e0 N\u1eb5ng, Vi\u1ec7t Nam',
            'footer.copyright': '&copy; 2025 Portfolio Platform. Built with <i class="fas fa-heart text-danger"></i> by Java Developer',
            title: 'Portfolio - Java Developer',
            typed: ['\u0110o\u00e0n \u0110\u00ecnh V\u0169', 'Java Developer', 'Spring Boot Developer', 'Problem Solver']
        }
    };

    let currentLanguage = localStorage.getItem('portfolioLanguage') || 'vi';
    let typedInstance = null;

    function getTranslation(key) {
        return translations[currentLanguage][key] || translations.en[key] || '';
    }

    function initializeTyped() {
        const typedText = document.getElementById('typed-text');
        if (!typedText || typeof Typed === 'undefined') return;

        if (typedInstance) {
            typedInstance.destroy();
        }

        typedInstance = new Typed('#typed-text', {
            strings: getTranslation('typed'),
            typeSpeed: 50,
            backSpeed: 30,
            loop: true
        });
    }

    function applyLanguage(language) {
        currentLanguage = translations[language] ? language : 'vi';
        localStorage.setItem('portfolioLanguage', currentLanguage);
        document.documentElement.lang = currentLanguage;
        document.title = getTranslation('title');

        document.querySelectorAll('[data-i18n]').forEach(element => {
            element.textContent = getTranslation(element.dataset.i18n);
        });

        document.querySelectorAll('[data-i18n-html]').forEach(element => {
            element.innerHTML = getTranslation(element.dataset.i18nHtml);
        });

        document.querySelectorAll('[data-i18n-attr]').forEach(element => {
            element.dataset.i18nAttr.split(',').forEach(pair => {
                const [attribute, key] = pair.split(':').map(value => value.trim());
                if (attribute && key) {
                    element.setAttribute(attribute, getTranslation(key));
                }
            });
        });

        initializeTyped();
    }

    const languageToggle = document.getElementById('languageToggle');
    if (languageToggle) {
        languageToggle.addEventListener('click', function () {
            applyLanguage(currentLanguage === 'vi' ? 'en' : 'vi');
        });
    }

    applyLanguage(currentLanguage);

    const landingContactForm = document.getElementById('landingContactForm');
    const landingContactFeedback = document.getElementById('landingContactFeedback');

    function setLandingContactFeedback(message, type) {
        if (!landingContactFeedback) return;

        landingContactFeedback.textContent = message;
        landingContactFeedback.classList.remove('success', 'error');
        if (type) {
            landingContactFeedback.classList.add(type);
        }
    }

    if (landingContactForm) {
        landingContactForm.addEventListener('submit', function (event) {
            event.preventDefault();

            const subjectInput = document.getElementById('landingContactSubject');
            const contentInput = document.getElementById('landingContactContent');
            const submitButton = landingContactForm.querySelector('button[type="submit"]');
            const subject = subjectInput ? subjectInput.value.trim() : '';
            const content = contentInput ? contentInput.value.trim() : '';

            if (!subject || !content) {
                setLandingContactFeedback(getTranslation('cta.validationError'), 'error');
                return;
            }

            const originalButtonHtml = submitButton ? submitButton.innerHTML : '';
            if (submitButton) {
                submitButton.disabled = true;
                submitButton.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>' + getTranslation('cta.sending');
            }
            setLandingContactFeedback('', '');

            fetch('/contact/send', {
                method: 'POST',
                body: new FormData(landingContactForm),
                headers: { 'X-Requested-With': 'XMLHttpRequest' }
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        landingContactForm.reset();
                        setLandingContactFeedback(getTranslation('cta.success'), 'success');
                    } else {
                        setLandingContactFeedback(data.message || getTranslation('cta.validationError'), 'error');
                    }
                })
                .catch(() => {
                    setLandingContactFeedback(getTranslation('cta.networkError'), 'error');
                })
                .finally(() => {
                    if (submitButton) {
                        submitButton.disabled = false;
                        submitButton.innerHTML = originalButtonHtml;
                    }
                });
        });
    }

    // Initialize AOS
    if (typeof AOS !== 'undefined') {
        AOS.init({ duration: 1000, once: true });
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
            const targetId = this.getAttribute('href');
            if (!targetId || !targetId.startsWith('#')) return;

            const target = document.querySelector(targetId);

            if (!target) return;

            e.preventDefault();

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
            if (!menu.contains(e.target) && (!menuToggle || !menuToggle.contains(e.target))) {
                menu.classList.remove('active');
                if (menuToggle) menuToggle.classList.remove('active');
            }
        }
    });

    // Initial active menu check
    updateActiveMenuOnScroll();
});
