export const profile = {
  name: 'Đoàn Đình Vũ',
  brand: 'Portfolio Vu.DD',
  title: 'Java Developer',
  email: 'markdoan38@gmail.com',
  phone: '0799077222',
  location: 'Đà Nẵng, Việt Nam',
  avatarUrl: 'https://res.cloudinary.com/dlbw54otc/image/upload/v1779155886/IMG_5814_vzf0bi.jpg',
};

export const services = [
  {
    icon: 'fas fa-server',
    title: 'Backend Development',
    description: 'Java, Spring Boot, REST APIs, WebSocket.',
  },
  {
    icon: 'fas fa-database',
    title: 'Database & Data Layer',
    description: 'MySQL, MongoDB, JPA/Hibernate, query optimization.',
  },
  {
    icon: 'fas fa-code-branch',
    title: 'Deployment & Collaboration',
    description: 'Docker basics, Git/GitHub, Agile/Scrum, production support.',
  },
];

export const projects = [
  {
    name: 'Car Infotainment System',
    description:
      'Java Developer on automotive infotainment projects at FPT Software, Android Framework.',
    image: 'https://placehold.co/600x400/2f80ed/white?text=Car+Infotainment',
    status: 'OPEN',
    skills: ['Java', 'Android', 'Spring Boot'],
  },
  {
    name: 'Real-time Chat Platform',
    description: 'Portfolio messaging platform using WebSocket, STOMP and Redis.',
    image: 'https://placehold.co/600x400/14b8a6/white?text=Chat+Platform',
    status: 'ACTIVE',
    skills: ['WebSocket', 'Redis', 'REST API'],
  },
  {
    name: 'Portfolio Platform',
    description: 'Spring Boot, MySQL, OAuth2 and a modern Angular frontend.',
    image: 'https://placehold.co/600x400/f59e0b/white?text=Portfolio+Platform',
    status: 'LIVE',
    skills: ['Spring Boot', 'MySQL', 'OAuth2'],
  },
];

export const skills = [
  { name: 'Java 17', category: 'Backend', percent: 90, years: 2, icon: 'fab fa-java' },
  { name: 'Spring Boot', category: 'Backend', percent: 88, years: 2, icon: 'fas fa-leaf' },
  { name: 'Spring Security', category: 'Security', percent: 82, years: 2, icon: 'fas fa-shield-alt' },
  { name: 'Hibernate/JPA', category: 'Database', percent: 84, years: 2, icon: 'fas fa-database' },
  { name: 'MySQL', category: 'Database', percent: 86, years: 2, icon: 'fas fa-database' },
  { name: 'MongoDB', category: 'Database', percent: 70, years: 1, icon: 'fas fa-server' },
  { name: 'WebSocket', category: 'Realtime', percent: 76, years: 1, icon: 'fas fa-comments' },
  { name: 'REST API', category: 'API', percent: 90, years: 2, icon: 'fas fa-plug' },
  { name: 'Docker', category: 'DevOps', percent: 68, years: 1, icon: 'fab fa-docker' },
  { name: 'Git/GitHub', category: 'Collaboration', percent: 86, years: 2, icon: 'fab fa-github' },
  { name: 'Maven/Gradle', category: 'Build tools', percent: 78, years: 2, icon: 'fas fa-cogs' },
  { name: 'JIRA', category: 'Process', percent: 75, years: 2, icon: 'fas fa-list-check' },
];

export const conversations = [
  {
    subject: 'Cơ hội hợp tác',
    sender: 'Nguyễn Minh',
    email: 'minh@example.com',
    lastAt: '19/05/26 09:30',
    messages: [
      {
        fromMe: false,
        name: 'Nguyễn Minh',
        content: 'Chào Vũ, bên mình đang cần Java Developer cho dự án Spring Boot.',
        time: '19/05/2026 09:10',
      },
      {
        fromMe: true,
        name: 'Bạn',
        content: 'Cảm ơn anh, em sẵn sàng trao đổi thêm về scope dự án.',
        time: '19/05/2026 09:30',
      },
    ],
  },
  {
    subject: 'Review portfolio',
    sender: 'Admin',
    email: 'markdoan38@gmail.com',
    lastAt: '18/05/26 20:15',
    messages: [
      {
        fromMe: false,
        name: 'Admin',
        content: 'Landing page đã được chuyển sang Angular, phần contact dùng mock UI trước.',
        time: '18/05/2026 20:15',
      },
    ],
  },
];
