import { Search, Bell, Flame, Code, BookOpen, Target, Loader2, GraduationCap, Layers, LogOut, User } from 'lucide-react';
import { Link, useNavigate } from 'react-router';
import { useState, useEffect, useRef, useCallback } from 'react';
import axios from 'axios';
import { useAuth } from '../context/AuthContext';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from './ui/dropdown-menu';

// ─── Types ───────────────────────────────────────────────────────────────────

type ResultType = 'skill' | 'module' | 'course' | 'career path' | string;

/** Flat shape used by the UI */
interface SearchResult {
  id: string | number;
  title: string;
  type: ResultType;
  /** Optional direct URL override from the backend */
  url?: string;
}

// ─── Backend response shape ───────────────────────────────────────────────────
// Matches Java: SearchResponse { String resultType; List<?> result; }

interface BackendSearchResponse {
  resultType: string;          // e.g. "skill", "module", "course", "career path"
  result: Record<string, unknown>[];
}

/**
 * The backend may send:
 *   a) A single SearchResponse   → { resultType, result }
 *   b) An array of SearchResponse → [{ resultType, result }, ...]
 *
 * Each item inside `result` is a typed entity (Skill, Module, Course, CareerPath).
 * We try common title-field names per entity type to extract a human-readable label.
 */
function parseSearchResponse(
  raw: BackendSearchResponse | BackendSearchResponse[] | unknown
): SearchResult[] {
  if (!raw) return [];

  // Normalise to array
  const groups: BackendSearchResponse[] = Array.isArray(raw)
    ? (raw as BackendSearchResponse[])
    : [raw as BackendSearchResponse];

  const flat: SearchResult[] = [];

  for (const group of groups) {
    const type = (group.resultType ?? '').toLowerCase() as ResultType;
    const items = Array.isArray(group.result) ? group.result : [];

    for (const item of items) {
      // Try common title-field names per entity type, then fall back to
      // any generic name/title field present on the object.
      const title = extractTitle(item, type);
      const id    = (item['id'] ?? item['skillId'] ?? item['moduleId'] ??
                     item['courseId'] ?? item['careerPathId'] ?? '') as string | number;

      if (title) {
        flat.push({ id, title, type, url: item['url'] as string | undefined });
      }
    }
  }

  return flat;
}

/** Picks the best human-readable title field from a raw entity object */
function extractTitle(item: Record<string, unknown>, type: string): string {
  // Type-specific priority fields
  const candidatesByType: Record<string, string[]> = {
    skill:         ['skillName', 'name', 'title'],
    module:        ['moduleName', 'name', 'title'],
    course:        ['courseName', 'name', 'title'],
    'career path': ['pathName', 'careerPathName', 'name', 'title'],
  };

  const candidates = candidatesByType[type] ?? ['name', 'title'];

  // Try type-specific candidates first
  for (const key of candidates) {
    if (typeof item[key] === 'string' && (item[key] as string).trim()) {
      return item[key] as string;
    }
  }

  // Generic fallback: any field that ends in 'Name' or 'Title'
  for (const key of Object.keys(item)) {
    if (/name|title/i.test(key) && typeof item[key] === 'string') {
      return item[key] as string;
    }
  }

  return '';
}

// ─── Helpers ─────────────────────────────────────────────────────────────────

/** Build the navigation path based on result type + id */
function buildResultUrl(result: SearchResult): string {
  if (result.url) return result.url;

  const id = result.id;
  switch (result.type.toLowerCase()) {
    case 'skill':
      return id ? `/skills?highlight=${encodeURIComponent(id)}` : '/skills';
    case 'module':
      return id ? `/learning-sheet?moduleId=${encodeURIComponent(id)}` : '/learning-sheet';
    case 'course':
      return id ? `/recommendations?courseId=${encodeURIComponent(id)}` : '/recommendations';
    case 'career path':
      return id ? `/career-path?careerId=${encodeURIComponent(id)}` : '/career-path';
    default:
      return '#';
  }
}

/** Icon for each result type */
function TypeIcon({ type, size = 16 }: { type: string; size?: number }) {
  switch (type.toLowerCase()) {
    case 'skill':
      return <Code className="text-blue-500" size={size} />;
    case 'course':
      return <GraduationCap className="text-emerald-500" size={size} />;
    case 'module':
      return <Layers className="text-amber-500" size={size} />;
    case 'career path':
      return <Target className="text-purple-500" size={size} />;
    default:
      return <BookOpen className="text-slate-400" size={size} />;
  }
}

/** Badge colour classes for each result type */
function typeBadgeClasses(type: string): string {
  switch (type.toLowerCase()) {
    case 'skill':
      return 'bg-blue-50 text-blue-700 border-blue-200 ring-blue-500/10';
    case 'course':
      return 'bg-emerald-50 text-emerald-700 border-emerald-200 ring-emerald-500/10';
    case 'module':
      return 'bg-amber-50 text-amber-700 border-amber-200 ring-amber-500/10';
    case 'career path':
      return 'bg-purple-50 text-purple-700 border-purple-200 ring-purple-500/10';
    default:
      return 'bg-slate-50 text-slate-600 border-slate-200 ring-slate-500/10';
  }
}

/** Icon wrapper bg colour for each type */
function typeIconBg(type: string): string {
  switch (type.toLowerCase()) {
    case 'skill':       return 'bg-blue-50   border-blue-100';
    case 'course':      return 'bg-emerald-50 border-emerald-100';
    case 'module':      return 'bg-amber-50   border-amber-100';
    case 'career path': return 'bg-purple-50  border-purple-100';
    default:            return 'bg-slate-50   border-slate-100';
  }
}

// ─── Component ───────────────────────────────────────────────────────────────

export function Header({ streak }: { streak: number | string }) {
  const navigate = useNavigate();
  const { email, logout } = useAuth();

  const handleLogout = useCallback(() => {
    logout();
    navigate('/auth');
  }, [logout, navigate]);

  const [query, setQuery]         = useState('');
  const [results, setResults]     = useState<SearchResult[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [isOpen, setIsOpen]       = useState(false);
  const [activeIdx, setActiveIdx] = useState(-1);

  const wrapperRef = useRef<HTMLDivElement>(null);
  const inputRef   = useRef<HTMLInputElement>(null);

  // ── Close on outside click ─────────────────────────────────────────────────
  useEffect(() => {
    const handler = (e: MouseEvent) => {
      if (wrapperRef.current && !wrapperRef.current.contains(e.target as Node)) {
        setIsOpen(false);
        setActiveIdx(-1);
      }
    };
    document.addEventListener('mousedown', handler);
    return () => document.removeEventListener('mousedown', handler);
  }, []);

  // ── Debounced API search (fires 350 ms after user stops typing) ────────────
  useEffect(() => {
    const trimmed = query.trim();
    if (!trimmed) {
      setResults([]);
      setIsOpen(false);
      setActiveIdx(-1);
      return;
    }

    setIsLoading(true);
    setIsOpen(true);

    const timer = setTimeout(async () => {
      try {
        const res = await axios.get(
          `http://localhost:8080/api/search/search?keyword=${encodeURIComponent(trimmed)}`
        );
        // res.data is either SearchResponse or SearchResponse[]
        setResults(parseSearchResponse(res.data));
      } catch (err) {
        console.error('Search failed:', err);
        setResults([]);
      } finally {
        setIsLoading(false);
        setActiveIdx(-1);
      }
    }, 350);

    return () => clearTimeout(timer);
  }, [query]);

  // ── Navigate to a result ───────────────────────────────────────────────────
  const handleSelect = useCallback(
    (result: SearchResult) => {
      const url = buildResultUrl(result);
      setIsOpen(false);
      setQuery('');
      setResults([]);
      setActiveIdx(-1);
      navigate(url);
    },
    [navigate]
  );

  // ── Keyboard navigation ────────────────────────────────────────────────────
  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (!isOpen || results.length === 0) return;

    switch (e.key) {
      case 'ArrowDown':
        e.preventDefault();
        setActiveIdx(prev => (prev + 1) % results.length);
        break;
      case 'ArrowUp':
        e.preventDefault();
        setActiveIdx(prev => (prev - 1 + results.length) % results.length);
        break;
      case 'Enter':
        if (activeIdx >= 0 && results[activeIdx]) {
          e.preventDefault();
          handleSelect(results[activeIdx]);
        }
        break;
      case 'Escape':
        setIsOpen(false);
        setActiveIdx(-1);
        inputRef.current?.blur();
        break;
    }
  };

  // ─── Render ─────────────────────────────────────────────────────────────────
  return (
    <div className="bg-white/80 backdrop-blur-lg border-b border-slate-200 px-8 py-4 flex items-center justify-between z-40 relative">

      {/* ── Search bar ── */}
      <div className="flex-1 max-w-2xl" ref={wrapperRef}>
        <div className="relative group">

          {/* Leading search icon */}
          <Search
            className="absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400 group-focus-within:text-blue-500 transition-colors pointer-events-none"
            size={18}
          />

          {/* Input */}
          <input
            ref={inputRef}
            type="text"
            value={query}
            onChange={e => setQuery(e.target.value)}
            onFocus={() => { if (query.trim()) setIsOpen(true); }}
            onKeyDown={handleKeyDown}
            placeholder="Search skills, courses, modules, career paths…"
            aria-label="Search"
            aria-autocomplete="list"
            aria-expanded={isOpen}
            className="w-full pl-10 pr-10 py-2.5 bg-slate-50/60 border border-slate-200 rounded-xl
                       focus:bg-white focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-400
                       transition-all text-sm text-slate-800 placeholder:text-slate-400 shadow-sm"
          />

          {/* Trailing spinner */}
          {isLoading && (
            <Loader2
              className="absolute right-3.5 top-1/2 -translate-y-1/2 text-blue-500 animate-spin pointer-events-none"
              size={16}
            />
          )}

          {/* ── Dropdown ── */}
          {isOpen && (
            <div
              className="absolute top-[calc(100%+8px)] left-0 right-0
                         bg-white border border-slate-200 rounded-xl shadow-2xl shadow-slate-300/30
                         overflow-hidden z-50 max-h-[420px] overflow-y-auto
                         animate-in fade-in slide-in-from-top-2 duration-150"
              role="listbox"
            >
              {isLoading ? (
                /* Loading skeleton */
                <div className="py-3">
                  {[1, 2, 3].map(i => (
                    <div key={i} className="flex items-center gap-3 px-4 py-3">
                      <div className="w-8 h-8 rounded-lg bg-slate-100 animate-pulse shrink-0" />
                      <div className="flex-1 space-y-2">
                        <div className="h-3 bg-slate-100 rounded-full animate-pulse w-3/4" />
                        <div className="h-2 bg-slate-100 rounded-full animate-pulse w-1/3" />
                      </div>
                      <div className="h-5 w-16 bg-slate-100 rounded-md animate-pulse" />
                    </div>
                  ))}
                </div>
              ) : results.length > 0 ? (
                /* Results list */
                <div className="py-1.5">
                  <p className="px-4 pt-1 pb-2 text-[10px] font-bold uppercase tracking-widest text-slate-400 select-none">
                    {results.length} result{results.length !== 1 ? 's' : ''} found
                  </p>

                  {results.map((result, idx) => {
                    const isActive = idx === activeIdx;
                    return (
                      <button
                        key={`${result.type}-${result.id}-${idx}`}
                        role="option"
                        aria-selected={isActive}
                        onMouseEnter={() => setActiveIdx(idx)}
                        onMouseLeave={() => setActiveIdx(-1)}
                        onClick={() => handleSelect(result)}
                        className={`w-full text-left flex items-center gap-3 px-4 py-2.5 transition-all duration-100
                                    border-b border-slate-50 last:border-0 group/item cursor-pointer
                                    ${isActive ? 'bg-blue-50/70' : 'hover:bg-slate-50/80'}`}
                      >
                        {/* Type icon */}
                        <div className={`w-8 h-8 shrink-0 rounded-lg flex items-center justify-center border transition-transform duration-100 ${typeIconBg(result.type)} ${isActive ? 'scale-110' : 'group-hover/item:scale-110'}`}>
                          <TypeIcon type={result.type} size={15} />
                        </div>

                        {/* Title */}
                        <div className="flex-1 min-w-0">
                          <p className={`text-sm font-semibold truncate transition-colors ${isActive ? 'text-blue-700' : 'text-slate-900 group-hover/item:text-blue-600'}`}>
                            {result.title}
                          </p>
                        </div>

                        {/* Type badge */}
                        <span className={`shrink-0 text-[10px] px-2 py-0.5 rounded-md font-bold border capitalize ring-1 ring-inset ${typeBadgeClasses(result.type)}`}>
                          {result.type}
                        </span>
                      </button>
                    );
                  })}
                </div>
              ) : (
                /* Empty state */
                <div className="py-10 flex flex-col items-center justify-center text-center gap-2">
                  <div className="w-12 h-12 rounded-full bg-slate-100 flex items-center justify-center mb-1">
                    <Search className="text-slate-300" size={22} />
                  </div>
                  <p className="text-sm font-semibold text-slate-700">No results for "<span className="text-blue-600">{query}</span>"</p>
                  <p className="text-xs text-slate-400">Try a different skill, course, or career path</p>
                </div>
              )}
            </div>
          )}
        </div>
      </div>

      {/* ── Right controls ── */}
      <div className="flex items-center gap-5 ml-6">

        {/* Streak badge */}
        <div className="flex items-center gap-2 bg-gradient-to-r from-orange-50 to-amber-50 px-4 py-2 rounded-xl shadow-sm border border-orange-100/60">
          <Flame className="text-orange-500 animate-pulse" size={18} fill="currentColor" />
          <span className="font-bold text-orange-700 text-sm">
            {streak} <span className="font-medium text-orange-600/80">day streak</span>
          </span>
        </div>

        <div className="h-7 w-px bg-slate-200" />

        {/* Bell */}
        <button className="relative p-2 text-slate-500 hover:text-slate-900 hover:bg-slate-100 rounded-xl transition-all">
          <Bell size={19} />
          <span className="absolute top-1.5 right-1.5 w-2 h-2 bg-red-500 rounded-full border-2 border-white shadow-sm" />
        </button>

        {/* Avatar Dropdown */}
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <button
              className="w-9 h-9 bg-gradient-to-tr from-blue-600 to-indigo-600
                         hover:from-blue-700 hover:to-indigo-700
                         shadow-md hover:shadow-lg hover:-translate-y-0.5 transition-all
                         rounded-full flex items-center justify-center
                         text-white text-xs font-bold tracking-wide border-2 border-white focus:outline-none cursor-pointer"
              title="User Account"
            >
              {email ? email.substring(0, 2).toUpperCase() : 'U'}
            </button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end" className="w-56 bg-white rounded-xl shadow-lg border border-slate-200 p-1">
            <DropdownMenuLabel className="px-3 py-2">
              <div className="text-xs text-slate-500 font-normal">Signed in as</div>
              <div className="text-sm font-semibold text-slate-800 truncate">{email || 'User'}</div>
            </DropdownMenuLabel>
            <DropdownMenuSeparator className="bg-slate-100" />
            <DropdownMenuItem
              onClick={() => navigate('/profile')}
              className="cursor-pointer flex items-center gap-2 px-3 py-2 text-sm text-slate-700 hover:bg-slate-100 rounded-md"
            >
              <User size={16} />
              <span>My Profile</span>
            </DropdownMenuItem>
            <DropdownMenuSeparator className="bg-slate-100" />
            <DropdownMenuItem
              onClick={handleLogout}
              className="cursor-pointer flex items-center gap-2 px-3 py-2 text-sm text-red-600 hover:bg-red-50 rounded-md font-medium"
            >
              <LogOut size={16} />
              <span>Logout</span>
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>
    </div>
  );
}
