import { useState, useEffect } from 'react';
import { useSearchParams, Link, useNavigate } from 'react-router';
import { ArrowLeft, BookOpen, Clock, Code, Trophy, Check, AlertTriangle, RefreshCw, ServerCrash, Loader2 } from 'lucide-react';
import axios from 'axios';
import { API_BASE_URL } from '../utils/api';

interface Module {
  id: number;
  title: string;
  durationWeeks: number;
  skills: string[];
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

// ─── Error type helpers ─────────────────────────────────────────────────────
type ErrorKind = 'ai_overload' | 'server' | 'empty' | 'network';

interface FetchError {
  kind: ErrorKind;
  message: string;
}

const classifyError = (err: unknown): FetchError => {
  if (axios.isAxiosError(err)) {
    const status = err.response?.status;
    if (status === 503 || status === 429) {
      return {
        kind: 'ai_overload',
        message: 'The AI is currently experiencing high demand. Please try again in a moment.',
      };
    }
    if (status && status >= 500) {
      return {
        kind: 'server',
        message: `Server error (${status}). Our backend ran into an issue. Please try again.`,
      };
    }
    if (!err.response) {
      return {
        kind: 'network',
        message: 'Could not reach the server. Please check your connection and try again.',
      };
    }
  }
  return {
    kind: 'server',
    message: 'Something went wrong while loading the roadmap. Please try again.',
  };
};

const ErrorBanner = ({
  error,
  onRetry,
}: {
  error: FetchError;
  onRetry: () => void;
}) => {
  const icons: Record<ErrorKind, JSX.Element> = {
    ai_overload: <AlertTriangle className="w-8 h-8 text-yellow-500" />,
    server:      <ServerCrash  className="w-8 h-8 text-red-500" />,
    empty:       <AlertTriangle className="w-8 h-8 text-yellow-500" />,
    network:     <ServerCrash  className="w-8 h-8 text-red-500" />,
  };

  const bgColors: Record<ErrorKind, string> = {
    ai_overload: 'bg-yellow-50 border-yellow-200',
    server:      'bg-red-50 border-red-200',
    empty:       'bg-yellow-50 border-yellow-200',
    network:     'bg-red-50 border-red-200',
  };

  return (
    <div className={`flex flex-col items-center justify-center py-24 px-8 gap-6 text-center`}>
      <div className={`flex flex-col items-center gap-4 max-w-md p-8 rounded-2xl border ${bgColors[error.kind]}`}>
        {icons[error.kind]}
        <div>
          <h2 className="text-lg font-bold text-gray-800 mb-1">
            {error.kind === 'ai_overload' ? '⚡ AI Overloaded — Try Again Later' : '⚠️ Failed to Load Roadmap'}
          </h2>
          <p className="text-sm text-gray-600">{error.message}</p>
        </div>
        <button
          onClick={onRetry}
          className="flex items-center gap-2 px-5 py-2.5 bg-slate-900 text-white rounded-lg text-sm font-semibold hover:bg-slate-800 transition-colors"
        >
          <RefreshCw className="w-4 h-4" />
          Retry
        </button>
      </div>
    </div>
  );
};

export function CareerPathPage() {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const roleQuery  = searchParams.get('role')   || 'Software Engineer';
  const roleIdQuery = searchParams.get('roleId');

  const cacheKey = `cachedRoadmap_v3_${roleQuery.toLowerCase().trim()}`;

  const [activeRoadmap, setActiveRoadmap] = useState<RoadmapData | null>(() => {
    const cached = sessionStorage.getItem(cacheKey);
    if (cached) {
      try { return JSON.parse(cached); } catch { /* ignore */ }
    }
    return null;
  });
  const [loading, setLoading] = useState<boolean>(!sessionStorage.getItem(cacheKey));
  const [fetchError, setFetchError] = useState<FetchError | null>(null);

  const storageKey = `skill_route_completed_${roleQuery.toLowerCase().trim().replace(/\s+/g, '_')}`;
  const [completedModuleIds, setCompletedModuleIds] = useState<number[]>(() => {
    const saved = localStorage.getItem(storageKey);
    if (saved) {
      try { return JSON.parse(saved); } catch { /* ignore */ }
    }
    return [];
  });

  const fetchRoadmap = async () => {
    setLoading(true);
    setFetchError(null);
    try {
      const response = await axios.post(`${API_BASE_URL}/api/roadmap/get_roadmap`, {
        roleTitle: roleQuery,
      });

      const data = response.data;

      // Backend returned null or empty (AI fallback fired)
      if (!data || (!data.title && (!data.phases || data.phases.length === 0))) {
        setFetchError({
          kind: 'ai_overload',
          message: 'The AI is currently experiencing high demand and could not generate the roadmap. Please try again in a moment.',
        });
        setActiveRoadmap(null);
        return;
      }

      const normalizedRoadmap: RoadmapData = {
        roleId:      data.roleId,
        title:       data.title       || roleQuery,
        description: data.description || '',
        totalPhases: data.totalPhases,
        phases: (data.phases || []).map((phase: any) => ({
          id:          phase.id          || phase.phaseNumber,
          phaseName:   phase.phaseName   || phase.title || `Phase ${phase.phaseNumber || ''}`,
          description: phase.description || '',
          phaseNumber: phase.phaseNumber || phase.id || 0,
          modules: (phase.modules || []).map((moduleWrapper: any) => {
            const mod        = moduleWrapper.module || moduleWrapper;
            const skillsList = moduleWrapper.skills || mod.skills || [];
            return {
              id:            mod.id,
              title:         mod.title || mod.moduleName || '',
              durationWeeks: mod.durationWeeks || parseInt(mod.duration) || 0,
              skills: skillsList.map((s: any) =>
                typeof s === 'string' ? s : (s.title || s.name || s.skillName || '')
              ),
            };
          }),
        })),
      };

      setActiveRoadmap(normalizedRoadmap);
      sessionStorage.setItem(cacheKey, JSON.stringify(normalizedRoadmap));
    } catch (err) {
      console.error('Failed to fetch roadmap:', err);
      setFetchError(classifyError(err));
      setActiveRoadmap(null);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (!sessionStorage.getItem(cacheKey)) {
      fetchRoadmap();
    }
  }, [roleQuery, roleIdQuery]);

  // Keep completed modules in sync with localStorage
  useEffect(() => {
    const saved = localStorage.getItem(storageKey);
    if (saved) {
      try { setCompletedModuleIds(JSON.parse(saved)); } catch { /* ignore */ }
    }
  }, [storageKey]);

  const toggleModuleCompletion = (moduleId: number) => {
    setCompletedModuleIds((prev) => {
      const updated = prev.includes(moduleId)
        ? prev.filter((id) => id !== moduleId)
        : [...prev, moduleId];
      localStorage.setItem(storageKey, JSON.stringify(updated));
      return updated;
    });
  };

  // ─── Loading state ────────────────────────────────────────────────────────
  if (loading) {
    return (
      <div className="p-8 bg-gray-50 min-h-screen flex items-center justify-center">
        <div className="flex flex-col items-center gap-4">
          <Loader2 className="w-12 h-12 text-slate-900 animate-spin" />
          <p className="text-gray-500 font-medium">Generating your personalised roadmap…</p>
          <p className="text-xs text-gray-400">This may take a few seconds</p>
        </div>
      </div>
    );
  }

  // ─── Error state ──────────────────────────────────────────────────────────
  if (fetchError || !activeRoadmap) {
    return (
      <div className="p-8 bg-gray-50 min-h-screen">
        <div className="max-w-7xl mx-auto">
          <Link
            to="/careers"
            className="inline-flex items-center gap-2 text-sm font-medium text-gray-500 hover:text-gray-800 mb-6 transition-colors"
          >
            <ArrowLeft className="w-4 h-4" />
            Back to Career Paths
          </Link>
          <ErrorBanner
            error={fetchError ?? { kind: 'empty', message: 'No roadmap data was returned by the server.' }}
            onRetry={() => {
              sessionStorage.removeItem(cacheKey);
              fetchRoadmap();
            }}
          />
        </div>
      </div>
    );
  }

  // ─── Success state ────────────────────────────────────────────────────────
  const allModules         = activeRoadmap.phases.flatMap((p) => p.modules);
  const totalCompletedCount = allModules.filter((m) => completedModuleIds.includes(m.id)).length;
  const totalModulesCount  = allModules.length;
  const overallProgress    = totalModulesCount > 0 ? Math.round((totalCompletedCount / totalModulesCount) * 100) : 0;

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
            <h1 className="text-3xl font-bold text-gray-900 mb-2">{activeRoadmap.title}</h1>
            <p className="text-gray-600">{activeRoadmap.description}</p>
          </div>

          <div className="text-left md:text-right shrink-0">
            <span className="text-xs font-semibold text-gray-400 block tracking-wider uppercase mb-0.5">
              Overall Progress
            </span>
            <div className="flex items-center md:justify-end gap-2">
              <span className="text-3xl font-extrabold text-gray-900">{overallProgress}%</span>
              <Trophy className="text-amber-500 w-8 h-8 fill-amber-100" />
            </div>
          </div>
        </div>

        {/* Phases */}
        <div className="relative pl-8 md:pl-12">
          {activeRoadmap.phases.map((phase, phaseIndex) => {
            const phaseModules  = phase.modules;
            const completedCount = phaseModules.filter((m) => completedModuleIds.includes(m.id)).length;
            const totalCount    = phaseModules.length;
            const phaseProgress = totalCount > 0 ? Math.round((completedCount / totalCount) * 100) : 0;
            const isLastPhase   = phaseIndex === activeRoadmap.phases.length - 1;

            return (
              <div key={phase.id} className="relative pb-16 last:pb-4">
                {/* Vertical line */}
                {!isLastPhase && (
                  <div className="absolute left-[-2rem] md:left-[-3rem] top-12 bottom-0 w-0.5 bg-gray-200 -translate-x-1/2" />
                )}

                {/* Phase badge */}
                <div className="absolute left-[-2rem] md:left-[-3rem] top-0 w-10 h-10 md:w-12 md:h-12 rounded-full bg-slate-950 text-white flex items-center justify-center font-bold text-base md:text-lg border-4 border-white shadow-sm -translate-x-1/2 select-none">
                  {phase.phaseNumber || phase.id}
                </div>

                {/* Phase info */}
                <div className="mb-8 pl-2">
                  <h2 className="text-2xl font-bold text-gray-900 mb-1">
                    Phase {phase.phaseNumber || phase.id}: {phase.phaseName}
                  </h2>
                  <p className="text-gray-500 mb-3">{phase.description}</p>

                  <div className="flex items-center gap-3">
                    <div className="w-48 md:w-64 bg-gray-200 h-2 rounded-full overflow-hidden">
                      <div
                        className="bg-slate-950 h-full rounded-full transition-all duration-500"
                        style={{ width: `${phaseProgress}%` }}
                      />
                    </div>
                    <span className="text-xs font-semibold text-gray-500">{phaseProgress}% Complete</span>
                  </div>
                </div>

                {/* Module cards */}
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
                        {isCompleted && (
                          <div className="absolute -top-2.5 -right-2.5 w-6 h-6 bg-emerald-500 rounded-full flex items-center justify-center text-white border-2 border-white shadow-sm">
                            <Check className="w-3.5 h-3.5" strokeWidth={3} />
                          </div>
                        )}

                        <div className="space-y-4">
                          <div className="flex justify-between items-start gap-3">
                            <h3 className="font-bold text-gray-800 text-lg leading-snug">{module.title}</h3>
                            <BookOpen className="text-gray-400 w-5 h-5 shrink-0" />
                          </div>

                          <div className="flex items-center gap-1.5 text-gray-500 text-sm">
                            <Clock className="w-4 h-4 text-gray-400" />
                            <span>
                              {module.durationWeeks} {module.durationWeeks === 1 ? 'week' : 'weeks'}
                            </span>
                          </div>

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
                                  <Code className="w-3 h-3 text-gray-400 shrink-0" />
                                  {skill}
                                </span>
                              ))}
                            </div>
                          </div>
                        </div>

                        <div className="mt-6">
                          <button
                            onClick={() =>
                              navigate(
                                `/learning-sheet?moduleId=${module.id}&moduleName=${encodeURIComponent(module.title)}`
                              )
                            }
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
