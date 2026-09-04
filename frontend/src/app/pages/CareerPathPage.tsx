import { useState, useEffect } from 'react';
import { useSearchParams, Link, useNavigate } from 'react-router';
import { ArrowLeft, BookOpen, Clock, Code, Trophy, Check } from 'lucide-react';
import axios from 'axios';

interface MockModule {
  id: number;
  title: string;
  duration: string;
  skills: string[];
  completed: boolean;
}

interface MockPhase {
  id: number;
  title: string;
  description: string;
  modules: MockModule[];
}

interface MockRoadmapData {
  title: string;
  description: string;
  phases: MockPhase[];
}

interface Module {
  id: number;
  title: string;
  durationWeeks: number;
  skills: string[];
  completed?: boolean;
}

interface Phase {
  id: number;
  phaseName: string;
  description: string;
  phaseNumber: number;
  modules: Module[];
}

interface RoadmapData {
  roleId?: number;
  title: string;
  description: string;
  totalPhases?: number;
  phases: Phase[];
}

const ROADMAPS: Record<string, MockRoadmapData> = {
  'software engineer': {
    title: 'Software Engineer Roadmap',
    description: 'Your personalized learning path to becoming a successful software engineer',
    phases: [
      {
        id: 1,
        title: 'Foundations',
        description: 'Build your programming fundamentals',
        modules: [
          {
            id: 101,
            title: 'Programming Basics',
            duration: '4 weeks',
            skills: ['Variables', 'Loops', 'Functions', 'Arrays'],
            completed: true,
          },
          {
            id: 102,
            title: 'Object-Oriented Programming',
            duration: '3 weeks',
            skills: ['Classes', 'Inheritance', 'Polymorphism', 'Encapsulation'],
            completed: true,
          },
          {
            id: 103,
            title: 'Time & Space Complexity',
            duration: '2 weeks',
            skills: ['Big O Notation', 'Algorithm Analysis', 'Optimization'],
            completed: false,
          },
        ],
      },
      {
        id: 2,
        title: 'Data Structures',
        description: 'Master essential data structures',
        modules: [
          {
            id: 104,
            title: 'Arrays & Linked Lists',
            duration: '3 weeks',
            skills: ['Arrays', 'Linked Lists', 'Stacks', 'Queues'],
            completed: false,
          },
          {
            id: 105,
            title: 'Trees & Graphs',
            duration: '4 weeks',
            skills: ['Binary Trees', 'BST', 'Graph Traversal', 'DFS/BFS'],
            completed: false,
          },
          {
            id: 106,
            title: 'Hashing & Searching',
            duration: '2 weeks',
            skills: ['HashMaps', 'Binary Search', 'Sorting Algorithms'],
            completed: false,
          },
        ],
      },
      {
        id: 3,
        title: 'Core CS & System Design',
        description: 'Learn core computer science concepts',
        modules: [
          {
            id: 107,
            title: 'Operating Systems & Networks',
            duration: '3 weeks',
            skills: ['Processes', 'Memory Management', 'TCP/IP', 'HTTP'],
            completed: false,
          },
          {
            id: 108,
            title: 'Database Fundamentals',
            duration: '3 weeks',
            skills: ['Relational DBs', 'SQL', 'Indexing', 'Transactions'],
            completed: false,
          },
        ],
      },
    ],
  },
  'frontend developer': {
    title: 'Frontend Developer Roadmap',
    description: 'Your personalized learning path to becoming a successful frontend developer',
    phases: [
      {
        id: 1,
        title: 'Web Foundations',
        description: 'Learn HTML, CSS, and basic Javascript',
        modules: [
          {
            id: 201,
            title: 'HTML & CSS Basics',
            duration: '3 weeks',
            skills: ['Semantic HTML', 'Flexbox', 'CSS Grid', 'Responsive Design'],
            completed: true,
          },
          {
            id: 202,
            title: 'Javascript Fundamentals',
            duration: '4 weeks',
            skills: ['DOM Manipulation', 'Fetch API', 'ES6+ Features', 'Event Loop'],
            completed: true,
          },
          {
            id: 203,
            title: 'Modern CSS Tools',
            duration: '2 weeks',
            skills: ['Tailwind CSS', 'SASS', 'CSS Modules', 'Styled Components'],
            completed: false,
          },
        ],
      },
      {
        id: 2,
        title: 'Frontend Frameworks',
        description: 'Master React and state management',
        modules: [
          {
            id: 204,
            title: 'React Essentials',
            duration: '4 weeks',
            skills: ['Components', 'JSX', 'Props & State', 'Hooks'],
            completed: false,
          },
          {
            id: 205,
            title: 'State Management',
            duration: '3 weeks',
            skills: ['Redux Toolkit', 'Context API', 'Zustand', 'Query fetching'],
            completed: false,
          },
          {
            id: 206,
            title: 'Next.js & Routing',
            duration: '3 weeks',
            skills: ['SSR', 'SSG', 'Dynamic Routes', 'API Handlers'],
            completed: false,
          },
        ],
      },
      {
        id: 3,
        title: 'Testing & Build Tools',
        description: 'Deploy and build production apps',
        modules: [
          {
            id: 207,
            title: 'Build Tools & Package Managers',
            duration: '2 weeks',
            skills: ['Vite', 'Webpack', 'npm/pnpm', 'Babel'],
            completed: false,
          },
          {
            id: 208,
            title: 'Testing Frontend Apps',
            duration: '2 weeks',
            skills: ['Jest', 'React Testing Library', 'Cypress'],
            completed: false,
          },
        ],
      },
    ],
  },
  'backend developer': {
    title: 'Backend Developer Roadmap',
    description: 'Your personalized learning path to becoming a successful backend developer',
    phases: [
      {
        id: 1,
        title: 'Programming & Databases',
        description: 'Learn backend languages and relational databases',
        modules: [
          {
            id: 301,
            title: 'Java / Node.js Basics',
            duration: '4 weeks',
            skills: ['Syntax', 'OOP', 'Async Programming', 'Package managers'],
            completed: true,
          },
          {
            id: 302,
            title: 'Relational Databases',
            duration: '3 weeks',
            skills: ['PostgreSQL', 'SQL Queries', 'Joins', 'Indexes', 'Schema Design'],
            completed: true,
          },
          {
            id: 303,
            title: 'Non-Relational Databases',
            duration: '2 weeks',
            skills: ['MongoDB', 'Redis caching', 'Document storage'],
            completed: false,
          },
        ],
      },
      {
        id: 2,
        title: 'Server & APIs',
        description: 'Build APIs and manage servers',
        modules: [
          {
            id: 304,
            title: 'RESTful API Design',
            duration: '3 weeks',
            skills: ['HTTP Methods', 'Status Codes', 'Request/Response', 'Express/Spring Boot'],
            completed: false,
          },
          {
            id: 305,
            title: 'Authentication & Security',
            duration: '3 weeks',
            skills: ['JWT', 'OAuth2', 'Password Hashing', 'Session Management'],
            completed: false,
          },
          {
            id: 306,
            title: 'Backend Testing',
            duration: '2 weeks',
            skills: ['Integration Testing', 'JUnit/Supertest', 'Mocking'],
            completed: false,
          },
        ],
      },
      {
        id: 3,
        title: 'DevOps & Architecture',
        description: 'Scale and deploy applications',
        modules: [
          {
            id: 307,
            title: 'Docker & Containerization',
            duration: '3 weeks',
            skills: ['Containers', 'Images', 'Docker Compose', 'Port mapping'],
            completed: false,
          },
          {
            id: 308,
            title: 'System Design & Scaling',
            duration: '3 weeks',
            skills: ['Load Balancers', 'Microservices', 'Message Queues'],
            completed: false,
          },
        ],
      },
    ],
  },
  'full stack developer': {
    title: 'Full Stack Developer Roadmap',
    description: 'Your personalized learning path to becoming a successful full stack developer',
    phases: [
      {
        id: 1,
        title: 'Frontend & Foundations',
        description: 'Master the UI layer',
        modules: [
          {
            id: 401,
            title: 'HTML/CSS & React',
            duration: '4 weeks',
            skills: ['Tailwind CSS', 'JSX', 'Hooks', 'Responsive Web'],
            completed: true,
          },
          {
            id: 402,
            title: 'Javascript & TypeScript',
            duration: '3 weeks',
            skills: ['Type safety', 'Interfaces', 'Async/Await'],
            completed: true,
          },
        ],
      },
      {
        id: 2,
        title: 'Backend & Database',
        description: 'Build the server and database',
        modules: [
          {
            id: 403,
            title: 'Node.js & Express',
            duration: '4 weeks',
            skills: ['REST APIs', 'Middleware', 'File upload', 'Routing'],
            completed: false,
          },
          {
            id: 404,
            title: 'Databases & ORMs',
            duration: '3 weeks',
            skills: ['PostgreSQL', 'Prisma', 'Mongoose', 'Migrations'],
            completed: false,
          },
        ],
      },
      {
        id: 3,
        title: 'Full Stack Integration',
        description: 'Connect everything together and deploy',
        modules: [
          {
            id: 405,
            title: 'Auth & Security',
            duration: '3 weeks',
            skills: ['JWT', 'CORS', 'Cookie-based sessions'],
            completed: false,
          },
          {
            id: 406,
            title: 'Deployment & CI/CD',
            duration: '3 weeks',
            skills: ['Vercel', 'Render', 'GitHub Actions', 'Environment Variables'],
            completed: false,
          },
        ],
      },
    ],
  },
  'android developer': {
    title: 'Android Developer Roadmap',
    description: 'Your personalized learning path to becoming a successful android developer',
    phases: [
      {
        id: 1,
        title: 'Kotlin Foundations',
        description: 'Learn the language of Android development',
        modules: [
          {
            id: 501,
            title: 'Kotlin Programming',
            duration: '4 weeks',
            skills: ['Variables', 'Null Safety', 'Coroutines', 'Standard Library'],
            completed: true,
          },
          {
            id: 502,
            title: 'OOP & Functional Programming',
            duration: '3 weeks',
            skills: ['Classes', 'Lambdas', 'Extension Functions'],
            completed: true,
          },
        ],
      },
      {
        id: 2,
        title: 'Android Basics',
        description: 'Build native user interfaces',
        modules: [
          {
            id: 503,
            title: 'Jetpack Compose',
            duration: '4 weeks',
            skills: ['Declarative UI', 'State', 'Modifiers', 'Composables'],
            completed: false,
          },
          {
            id: 504,
            title: 'Navigation & Architecture',
            duration: '3 weeks',
            skills: ['NavHost', 'ViewModel', 'LiveData', 'Clean Architecture'],
            completed: false,
          },
          {
            id: 505,
            title: 'Networking & Data',
            duration: '3 weeks',
            skills: ['Retrofit', 'Room DB', 'JSON Parsing', 'Repository Pattern'],
            completed: false,
          },
        ],
      },
      {
        id: 3,
        title: 'Testing & Play Store',
        description: 'Polish and deploy your app',
        modules: [
          {
            id: 506,
            title: 'Android Testing',
            duration: '2 weeks',
            skills: ['Espresso', 'Unit testing ViewModels', 'Mockito'],
            completed: false,
          },
          {
            id: 507,
            title: 'App Performance & Publishing',
            duration: '2 weeks',
            skills: ['Profiler', 'Memory leaks', 'Play Console release'],
            completed: false,
          },
        ],
      },
    ],
  },
};

const convertMockToRoadmap = (mock: MockRoadmapData): RoadmapData => {
  return {
    title: mock.title,
    description: mock.description,
    phases: mock.phases.map((p) => ({
      id: p.id,
      phaseName: p.title,
      description: p.description,
      phaseNumber: p.id,
      modules: p.modules.map((m) => ({
        id: m.id,
        title: m.title,
        durationWeeks: parseInt(m.duration) || 0,
        skills: m.skills
      }))
    }))
  };
};

export function CareerPathPage() {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const roleQuery = searchParams.get('role') || 'Software Engineer';
  const roleIdQuery = searchParams.get('roleId');
  const normalizedQuery = roleQuery.toLowerCase().trim();

  // Find the matching roadmap
  let matchedKey = 'software engineer';
  let fallbackRoadmap = ROADMAPS['software engineer'];

  if (normalizedQuery.includes('full')) {
    fallbackRoadmap = ROADMAPS['full stack developer'];
    matchedKey = 'full stack developer';
  } else if (normalizedQuery.includes('front')) {
    fallbackRoadmap = ROADMAPS['frontend developer'];
    matchedKey = 'frontend developer';
  } else if (normalizedQuery.includes('back')) {
    fallbackRoadmap = ROADMAPS['backend developer'];
    matchedKey = 'backend developer';
  } else if (normalizedQuery.includes('android')) {
    fallbackRoadmap = ROADMAPS['android developer'];
    matchedKey = 'android developer';
  } else if (ROADMAPS[normalizedQuery]) {
    fallbackRoadmap = ROADMAPS[normalizedQuery];
    matchedKey = normalizedQuery;
  }
  const cacheKey = `cachedRoadmap_v2_${normalizedQuery}`;

  const [activeRoadmap, setActiveRoadmap] = useState<RoadmapData>(() => {
    const cached = sessionStorage.getItem(cacheKey);
    if (cached) {
      try {
        return JSON.parse(cached);
      } catch (e) {
        // ignore
      }
    }
    return convertMockToRoadmap(fallbackRoadmap);
  });
  const [loading, setLoading] = useState<boolean>(() => !sessionStorage.getItem(cacheKey));
  const [error, setError] = useState<string | null>(null);

  const storageKey = `skill_route_completed_${matchedKey.replace(/\s+/g, '_')}`;
  const [completedModuleIds, setCompletedModuleIds] = useState<number[]>([]);

  const getRoleId = (roleName: string): number => {
    const norm = roleName.toLowerCase();
    if (norm.includes('front')) return 2;
    if (norm.includes('back')) return 3;
    if (norm.includes('full')) return 4;
    if (norm.includes('android')) return 5;
    return 1; // default to software engineer
  };

  // Keep state in sync with local storage & fallback defaults
  useEffect(() => {
    const saved = localStorage.getItem(storageKey);
    if (saved) {
      try {
        setCompletedModuleIds(JSON.parse(saved));
        return;
      } catch (e) {
        // ignore
      }
    }
    if (activeRoadmap) {
      const defaults = activeRoadmap.phases
        .flatMap((p) => p.modules)
        .filter((m) => (m as any).completed)
        .map((m) => m.id);
      setCompletedModuleIds(defaults);
    }
  }, [storageKey, activeRoadmap]);

  // Fetch roadmap dynamically from the backend
  useEffect(() => {
    const fetchRoadmap = async () => {
      if (sessionStorage.getItem(cacheKey)) {
        setLoading(false);
        return;
      }
      try {
        setLoading(true);
        setError(null);
        
        const response = await axios.post('http://localhost:8080/api/roadmap/get_roadmap', {
          roleTitle: roleQuery,
          userId: 1
          
        });

        if (response.data) {
          const data = response.data;
          const normalizedRoadmap: RoadmapData = {
            title: data.title || fallbackRoadmap.title,
            description: data.description || fallbackRoadmap.description,
            phases: (data.phases || []).map((phase: any) => ({
              id: phase.id || phase.phaseNumber,
              phaseName: phase.phaseName || phase.title || `Phase ${phase.phaseNumber || ''}`,
              description: phase.description || '',
              phaseNumber: phase.phaseNumber || phase.id || 0,
              modules: (phase.modules || []).map((moduleWrapper: any) => {
                const mod = moduleWrapper.module || moduleWrapper;
                const skillsList = moduleWrapper.skills || mod.skills || [];
                return {
                  id: mod.id,
                  title: mod.title || mod.moduleName || '',
                  durationWeeks: mod.durationWeeks || (parseInt(mod.duration) || 0),
                  skills: skillsList.map((s: any) => typeof s === 'string' ? s : (s.title || s.name || s.skillName || ''))
                };
              })
            }))
          };
          setActiveRoadmap(normalizedRoadmap);
          sessionStorage.setItem(cacheKey, JSON.stringify(normalizedRoadmap));
        } else {
          setActiveRoadmap(convertMockToRoadmap(fallbackRoadmap));
        }
      } catch (err) {
        console.error('Failed to fetch dynamic roadmap, falling back to mock data:', err);
        setActiveRoadmap(convertMockToRoadmap(fallbackRoadmap));
      } finally {
        setLoading(false);
      }
    };

    fetchRoadmap();
  }, [roleQuery, roleIdQuery]);

  // Handle toggling module completion
  const toggleModuleCompletion = (moduleId: number) => {
    setCompletedModuleIds((prev) => {
      const updated = prev.includes(moduleId)
        ? prev.filter((id) => id !== moduleId)
        : [...prev, moduleId];
      localStorage.setItem(storageKey, JSON.stringify(updated));
      return updated;
    });
  };

  // Compute overall progress
  const allModules = activeRoadmap.phases.flatMap((p) => p.modules);
  const totalCompletedCount = allModules.filter((m) => completedModuleIds.includes(m.id)).length;
  const totalModulesCount = allModules.length;
  const overallProgress = totalModulesCount > 0 ? Math.round((totalCompletedCount / totalModulesCount) * 100) : 0;

  if (loading) {
    return (
      <div className="p-8 bg-gray-50 min-h-screen flex items-center justify-center">
        <div className="flex flex-col items-center gap-4">
          <div className="w-12 h-12 border-4 border-slate-900 border-t-transparent rounded-full animate-spin"></div>
          <p className="text-gray-500 font-medium">Loading your dynamic roadmap...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="p-8 bg-gray-50 min-h-screen">
      <div className="max-w-7xl mx-auto">
        {/* Back Button */}
        <Link
          to="/careers"
          className="inline-flex items-center gap-2 text-sm font-medium text-gray-500 hover:text-gray-800 mb-6 transition-colors"
        >
          <ArrowLeft className="w-4 h-4" />
          Back to Career Paths
        </Link>

        {/* Header */}
        <div className="flex flex-col md:flex-row md:items-center justify-between mb-12 gap-4">
          <div>
            <h1 className="text-3xl font-bold text-gray-900 mb-2">
              {activeRoadmap.title}
            </h1>
            <p className="text-gray-600">
              {activeRoadmap.description}
            </p>
          </div>

          <div className="text-left md:text-right shrink-0">
            <span className="text-xs font-semibold text-gray-400 block tracking-wider uppercase mb-0.5">
              Overall Progress
            </span>
            <div className="flex items-center md:justify-end gap-2">
              <span className="text-3xl font-extrabold text-gray-900">
                {overallProgress}%
              </span>
              <Trophy className="text-amber-500 w-8 h-8 fill-amber-100" />
            </div>
          </div>
        </div>

        {/* Phases container */}
        <div className="relative pl-8 md:pl-12">
          {activeRoadmap.phases.map((phase, phaseIndex) => {
            const phaseModules = phase.modules;
            const completedCount = phaseModules.filter((m) => completedModuleIds.includes(m.id)).length;
            const totalCount = phaseModules.length;
            const phaseProgress = totalCount > 0 ? Math.round((completedCount / totalCount) * 100) : 0;
            const isLastPhase = phaseIndex === activeRoadmap.phases.length - 1;

            return (
              <div key={phase.id} className="relative pb-16 last:pb-4">
                {/* Vertical Line */}
                {!isLastPhase && (
                  <div className="absolute left-[-2rem] md:left-[-3rem] top-12 bottom-0 w-0.5 bg-gray-200 -translate-x-1/2" />
                )}

                {/* Circle Number Badge */}
                <div className="absolute left-[-2rem] md:left-[-3rem] top-0 w-10 h-10 md:w-12 md:h-12 rounded-full bg-slate-950 text-white flex items-center justify-center font-bold text-base md:text-lg border-4 border-white shadow-sm -translate-x-1/2 select-none">
                  {phase.phaseNumber || phase.id}
                </div>

                {/* Phase Info */}
                <div className="mb-8 pl-2">
                  <h2 className="text-2xl font-bold text-gray-900 mb-1">
                    Phase {phase.phaseNumber || phase.id}: {phase.phaseName}
                  </h2>
                  <p className="text-gray-500 mb-3">
                    {phase.description}
                  </p>
                  
                  {/* Phase Progress Bar */}
                  <div className="flex items-center gap-3">
                    <div className="w-48 md:w-64 bg-gray-200 h-2 rounded-full overflow-hidden">
                      <div
                        className="bg-slate-950 h-full rounded-full transition-all duration-500"
                        style={{ width: `${phaseProgress}%` }}
                      />
                    </div>
                    <span className="text-xs font-semibold text-gray-500">
                      {phaseProgress}% Complete
                    </span>
                  </div>
                </div>

                {/* Module Cards Grid */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 pl-2">
                  {phaseModules.map((module) => {
                    const isCompleted = completedModuleIds.includes(module.id);

                    return (
                      <div
                        key={module.id}
                        className={`relative bg-white rounded-xl border p-5 shadow-sm hover:shadow-md transition-all duration-300 flex flex-col justify-between ${
                          isCompleted ? 'border-emerald-400 ring-1 ring-emerald-400/20' : 'border-gray-200'
                        }`}
                      >
                        {/* Completed overlapping checkmark badge */}
                        {isCompleted && (
                          <div className="absolute -top-2.5 -right-2.5 w-6 h-6 bg-emerald-500 rounded-full flex items-center justify-center text-white border-2 border-white shadow-sm animate-scale-in">
                            <Check className="w-3.5 h-3.5" strokeWidth={3} />
                          </div>
                        )}

                        <div className="space-y-4">
                          {/* Title & Icon Header */}
                          <div className="flex justify-between items-start gap-3">
                            <h3 className="font-bold text-gray-800 text-lg leading-snug">
                              {module.title}
                            </h3>
                            <BookOpen className="text-gray-400 w-5 h-5 shrink-0" />
                          </div>

                          {/* Duration */}
                          <div className="flex items-center gap-1.5 text-gray-500 text-sm">
                            <Clock className="w-4 h-4 text-gray-400" />
                            <span>{module.durationWeeks} {module.durationWeeks === 1 ? 'week' : 'weeks'}</span>
                          </div>

                          {/* Skills Section */}
                          <div>
                            <span className="text-xs font-semibold text-gray-400 block tracking-wider uppercase mb-2">
                              Skills Covered
                            </span>
                            <div className="flex flex-wrap gap-1.5">
                              {module.skills.map((skill) => (
                                <span
                                  key={skill}
                                  className="inline-flex items-center gap-1 px-2.5 py-1 rounded-md bg-gray-100 text-gray-700 text-xs font-medium border border-gray-150"
                                >
                                  <Code className="w-3 h-3 text-gray-400 shrink-0"/>
                                  {skill}
                                </span>
                              ))}
                            </div>
                          </div>
                        </div>

                        {/* Interactive Button */}
                        <div className="mt-6">
                          <button
                            onClick={() => navigate(`/learning-sheet?moduleId=${module.id}&moduleName=${encodeURIComponent(module.title)}`)}
                            className={`w-full py-2.5 rounded-lg text-sm font-semibold transition-all duration-200 cursor-pointer ${
                              isCompleted
                                ? 'bg-white hover:bg-gray-50 text-gray-700 border border-gray-200 shadow-sm'
                                : 'bg-slate-950 hover:bg-slate-900 text-white shadow-sm'
                            }`}
                          >
                            {isCompleted ? 'Review' : 'Start Learning'}
                          </button>
                        </div>
                      </div>
                    );
                  })}
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
}
