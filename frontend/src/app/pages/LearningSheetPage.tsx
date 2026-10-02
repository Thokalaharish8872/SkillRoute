import { useState, useEffect } from 'react';
import { useSearchParams, Link } from 'react-router';
import axios from 'axios';
import { API_BASE_URL } from '../utils/api';
import { 
  ChevronDown, 
  ChevronRight, 
  PlaySquare, 
  FileText, 
  ExternalLink, 
  PlusCircle, 
  Star,
  CheckCircle2,
  Lock,
  ArrowLeft,
  Search,
  RefreshCcw,
  Download,
  Shuffle,
  Loader2,
  Clock,
  Award,
  TrendingUp,
  Info,
  User
} from 'lucide-react';
import { StreakModal } from '../components/StreakModal';

// Interfaces for our Detailed Mock Data
interface Problem {
  id: string;
  title: string;
  videoResource?: string[];
  documentationResource?: string;
  practiceLink?: string;
  difficulty: string;
  description?: string;
  estimatedHours?: number;
  certificationUrl?: string;
  prerequisites?: string;
  demandLevel?: string;
}

interface SubTopic {
  id: string;
  title: string;
  problems: Problem[];
}

interface Topic {
  id: string;
  title: string;
  subTopics: SubTopic[];
}

interface SheetData {
  title: string;
  description: string;
  topics: Topic[];
}

export function LearningSheetPage() {
  const [searchParams] = useSearchParams();
  const moduleName = searchParams.get('moduleName') || 'Module';
  
  const moduleId = searchParams.get('moduleId');
  const skillsParam = searchParams.get('skills');

  const [sheetData, setSheetData] = useState<SheetData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [expandedTopics, setExpandedTopics] = useState<Record<string, boolean>>({'t_1': true, 't_2': true, 't_3': true, 't_4': true}); // expanded by default or track dynamically
  const [expandedSubTopics, setExpandedSubTopics] = useState<Record<string, boolean>>({'st_1_0': true});
  
  const [completedProblems, setCompletedProblems] = useState<string[]>([]);
  const [starredProblems, setStarredProblems] = useState<string[]>([]);
  
  const [showStreakModal, setShowStreakModal] = useState(false);
  const [currentStreak, setCurrentStreak] = useState(0);

  useEffect(() => {
    // Fetch initial streak on mount
    axios.get(`${API_BASE_URL}/api/streak/get_streak`)
      .then(res => setCurrentStreak(res.data.streak !== undefined ? res.data.streak : (res.data || 0)))
      .catch(console.error);
  }, []);

  useEffect(() => {
    // Open the first topic/subtopic by default
    if (sheetData?.topics && sheetData.topics.length > 0) {
      setExpandedTopics(prev => ({ ...prev, [sheetData.topics[0].id]: true }));
      if (sheetData.topics[0].subTopics && sheetData.topics[0].subTopics.length > 0) {
        setExpandedSubTopics(prev => ({ ...prev, [sheetData.topics[0].subTopics[0].id]: true }));
      }
    }
  }, [sheetData]);

  // Build sheet from skills list (when no moduleId / fallback)
  const buildSheetFromSkills = (skillsList: string[], title: string) => {
    const subTopics = [{
      id: 'st_0_0',
      title: 'Skills to Master',
      problems: skillsList.map((skill, idx) => ({
        id: `p_skill_${idx}`,
        title: skill,
        description: '',
        difficulty: 'Medium',
        estimatedHours: null,
        documentationResource: '',
        videoResource: [],
        certificationUrl: '',
        prerequisites: '',
        demandLevel: ''
      }))
    }];
    setSheetData({
      title,
      description: `Skills covered in this module.`,
      topics: [{ id: 't_0', title: 'Module Overview', subTopics }]
    });
    setLoading(false);
  };

  useEffect(() => {
    const fetchSheetData = async () => {
      try {
        setLoading(true);
        const response = await axios.get(`${API_BASE_URL}/api/modules/get_module`, {
          params: { moduleId: moduleId }
        });
        
        if (response.data) {
          const data = response.data;
          
          // Check if it's the new ModuleResponse wrapper format
          const hasModuleWrapper = data.module !== undefined && data.skills !== undefined;
          const isModuleStructure = data.moduleName !== undefined || data.skills !== undefined || hasModuleWrapper;
          
          if (isModuleStructure) {
            // Extract the module and skills data
            const moduleData = hasModuleWrapper ? data.module : data;
            const skillsList = hasModuleWrapper ? data.skills : (data.skills || []);
            
            // Group skills by category
            const groupedSkills = skillsList.reduce((acc: any, skill: any) => {
              if (!skill) return acc; // Skip if the skill is null or undefined
              
              if (typeof skill === 'string') {
                if (!acc['General']) acc['General'] = [];
                acc['General'].push({ title: skill, difficulty: 'Easy' });
              } else {
                const category = skill.category || 'General';
                if (!acc[category]) acc[category] = [];
                acc[category].push(skill);
              }
              return acc;
            }, {});
            
            const subTopics = Object.keys(groupedSkills).map((category, index) => ({
              id: `st_${moduleData?.id || '1'}_${index}`,
              title: category,
              problems: groupedSkills[category].map((skill: any, pIndex: number) => ({
                id: `p_${skill?.id || (index + '_' + pIndex)}`,
                title: skill?.title || skill || 'Unnamed Skill',
                description: skill?.description || '',
                difficulty: skill?.difficulty || 'Easy',
                estimatedHours: skill?.estimatedHours || null,
                documentationResource: skill?.documentationResource || skill?.DocumentationResource || '',
                videoResource: skill?.videoResource || [],
                certificationUrl: skill?.certificationUrl || '',
                prerequisites: skill?.prerequisites || '',
                demandLevel: skill?.demandLevel || ''
              }))
            }));

            setSheetData({
              title: moduleData.title || moduleData.moduleName || `${moduleName} - Master Sheet`,
              description: moduleData.durationWeeks ? `Duration: ${moduleData.durationWeeks} Weeks` : 'A structured learning path.',
              topics: [
                {
                  id: `t_${moduleData.id || '1'}`,
                  title: 'Module Overview',
                  subTopics: subTopics
                }
              ]
            });
          } else {
            setSheetData({
              title: data.title || `${moduleName} - Master Sheet`,
              description: data.description || 'A structured, step-by-step learning path designed to help you master the core concepts efficiently.',
              topics: data.topics || data.subTopics || []
            });
          }
        } else {
          setError('No data returned from the server.');
        }
      } catch (err: any) {
        console.error('Failed to fetch module details:', err);
        setError(err.message || 'Failed to load module data. Please check if your backend is running.');
      } finally {
        setLoading(false);
      }
    };

    if (moduleId) {
      fetchSheetData();
    } else {
      setError('No module ID provided. Please go back and select a module.');
      setLoading(false);
    }
  }, [moduleName, moduleId]);

  const toggleTopic = (id: string) => {
    setExpandedTopics(prev => ({ ...prev, [id]: !prev[id] }));
  };

  const toggleSubTopic = (id: string) => {
    setExpandedSubTopics(prev => ({ ...prev, [id]: !prev[id] }));
  };

  const toggleCompletion = async (id: string) => {
    const isCurrentlyCompleted = completedProblems.includes(id);

    // Optimistically update the checkbox state
    setCompletedProblems(prev =>
      isCurrentlyCompleted ? prev.filter(pId => pId !== id) : [...prev, id]
    );

    // Only fire the API & update streak when marking as DONE (not when unchecking)
    if (!isCurrentlyCompleted) {
      try {
        // Extract numeric skillId: "p_64" → 64, "p_skill_0" → skip (no real DB id)
        const numericSkillId = parseInt(id.replace('p_', ''), 10);

        const response = await axios.post(
          `${API_BASE_URL}/api/activity/update_activity`,
          { userId:0,
            skillId: isNaN(numericSkillId) ? 0 : numericSkillId}
        );

        const newStreak = response.data.streak ?? 0;
        setCurrentStreak(newStreak);

        // Jackson serializes boolean isFirstTask → "firstTask" or "isFirstTask"
        const isFirstTaskToday = response.data.isFirstTask === true || response.data.firstTask === true;

        // Only show the streak modal if this is today's FIRST completed task
        if (isFirstTaskToday) {
          setShowStreakModal(true);
        }
      } catch (error) {
        console.error('Failed to update streak:', error);
      }
    }
  };

  const toggleStar = (id: string) => {
    setStarredProblems(prev => 
      prev.includes(id) ? prev.filter(pId => pId !== id) : [...prev, id]
    );
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-slate-50 flex items-center justify-center">
        <div className="flex flex-col items-center gap-4">
          <Loader2 className="w-12 h-12 text-indigo-600 animate-spin" />
          <p className="text-slate-500 font-medium">Loading your syllabus...</p>
        </div>
      </div>
    );
  }

  if (error || !sheetData) {
    return (
      <div className="min-h-screen bg-slate-50 flex items-center justify-center p-4">
        <div className="bg-white border border-rose-200 rounded-xl p-8 max-w-md w-full text-center shadow-sm">
          <div className="w-16 h-16 bg-rose-100 text-rose-600 rounded-full flex items-center justify-center mx-auto mb-4">
            <Info className="w-8 h-8" />
          </div>
          <h2 className="text-xl font-bold text-slate-900 mb-2">Oops! Something went wrong</h2>
          <p className="text-slate-600 mb-6">{error || 'Could not load the learning sheet.'}</p>
          <Link to={-1 as any} className="inline-flex items-center justify-center px-4 py-2 bg-indigo-600 text-white rounded-lg font-medium hover:bg-indigo-700 transition-colors">
             Go Back
          </Link>
        </div>
      </div>
    );
  }

  // Compute Progress
  const allProblems = sheetData.topics?.flatMap(t => t.subTopics?.flatMap(st => st.problems || []) || []) || [];
  const totalProblems = allProblems.length;
  const totalCompleted = completedProblems.length;
  const overallProgress = totalProblems > 0 ? Math.round((totalCompleted / totalProblems) * 100) : 0;

  const easyTotal = allProblems.filter(p => p.difficulty === 'Easy').length;
  const easyCompleted = allProblems.filter(p => p.difficulty === 'Easy' && completedProblems.includes(p.id)).length;
  
  const mediumTotal = allProblems.filter(p => p.difficulty === 'Medium').length;
  const mediumCompleted = allProblems.filter(p => p.difficulty === 'Medium' && completedProblems.includes(p.id)).length;
  
  const hardTotal = allProblems.filter(p => p.difficulty === 'Hard').length;
  const hardCompleted = allProblems.filter(p => p.difficulty === 'Hard' && completedProblems.includes(p.id)).length;

  return (
    <div className="min-h-screen bg-[#fafafa] text-slate-900 font-sans pb-20">
      
      <div className="bg-white border-b border-slate-200 sticky top-0 z-30 shadow-sm px-4 sm:px-6 lg:px-8 py-4 mb-6">
        <div className="max-w-7xl mx-auto flex items-center gap-4">
          <Link to={-1 as any} className="p-2 rounded-full hover:bg-slate-100 text-slate-500 transition-colors shrink-0">
            <ArrowLeft className="w-5 h-5" />
          </Link>
          <h1 className="text-xl md:text-2xl font-bold text-slate-900 tracking-tight line-clamp-1">
            {sheetData.title} <span className="font-normal text-slate-500 text-lg hidden sm:inline">- {sheetData.description}</span>
          </h1>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        
        {/* Toolbar */}
        <div className="flex flex-wrap items-center justify-between gap-4 mb-6">
          <div className="flex bg-white border border-slate-200 rounded-lg p-1 shadow-sm">
            <button className="px-4 py-1.5 bg-slate-100 text-slate-800 font-bold rounded-md text-sm">All Problems</button>
            <button className="px-4 py-1.5 text-slate-600 font-medium hover:bg-slate-50 rounded-md text-sm">Revision</button>
          </div>
          <div className="flex items-center gap-3 flex-wrap">
            <div className="relative">
              <Search className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
              <input type="text" placeholder="Search..." className="pl-9 pr-4 py-2 border border-slate-200 rounded-lg text-sm bg-white focus:outline-none focus:ring-2 focus:ring-orange-500 shadow-sm" />
            </div>
            <button className="px-4 py-2 border border-slate-200 bg-white rounded-lg text-sm font-medium flex items-center gap-2 hover:bg-slate-50 shadow-sm">
              All problems <ChevronDown className="w-4 h-4 text-slate-400" />
            </button>
            <button className="px-4 py-2 border border-slate-200 bg-white rounded-lg text-sm font-medium flex items-center gap-2 hover:bg-slate-50 shadow-sm">
              Difficulty <ChevronDown className="w-4 h-4 text-slate-400" />
            </button>
            <button className="px-4 py-2 border border-slate-200 bg-white rounded-lg text-sm font-medium flex items-center gap-2 hover:bg-slate-50 shadow-sm">
              <Shuffle className="w-4 h-4 text-slate-400" /> Random Problem
            </button>
          </div>
        </div>

        {/* Progress Card */}
        <div className="bg-white border border-slate-200 rounded-xl p-6 md:p-8 mb-8 shadow-sm flex flex-col md:flex-row items-start md:items-center justify-between gap-8">
          <div className="flex items-center gap-6">
            <div className="relative w-20 h-20 flex items-center justify-center rounded-full border-[6px] border-orange-500 border-r-slate-100 border-b-slate-100 shrink-0">
              <span className="text-lg font-bold text-slate-900">{overallProgress}%</span>
            </div>
            <div>
              <h3 className="text-xl font-bold text-slate-900 mb-1">Overall Progress</h3>
              <p className="text-slate-500 font-bold text-lg"><span className="text-slate-900">{totalCompleted}</span> <span className="text-sm font-medium">/ {totalProblems}</span></p>
            </div>
          </div>
          <div className="flex flex-wrap items-center gap-6 text-sm font-medium">
            <div className="flex items-center gap-2 bg-slate-50 px-4 py-2 rounded-lg border border-slate-100">
              <span className="w-3 h-3 rounded-full bg-emerald-500"></span>
              <span className="text-slate-600 font-bold">Easy</span>
              <span className="text-slate-900 font-bold ml-1">{easyCompleted}<span className="text-slate-400 text-xs">/{easyTotal}</span></span>
            </div>
            <div className="flex items-center gap-2 bg-slate-50 px-4 py-2 rounded-lg border border-slate-100">
              <span className="w-3 h-3 rounded-full bg-amber-500"></span>
              <span className="text-slate-600 font-bold">Medium</span>
              <span className="text-slate-900 font-bold ml-1">{mediumCompleted}<span className="text-slate-400 text-xs">/{mediumTotal}</span></span>
            </div>
            <div className="flex items-center gap-2 bg-slate-50 px-4 py-2 rounded-lg border border-slate-100">
              <span className="w-3 h-3 rounded-full bg-rose-500"></span>
              <span className="text-slate-600 font-bold">Hard</span>
              <span className="text-slate-900 font-bold ml-1">{hardCompleted}<span className="text-slate-400 text-xs">/{hardTotal}</span></span>
            </div>
          </div>
        </div>

        {/* Accordions */}
        <div className="space-y-4">
          {sheetData.topics.map((topic, tIndex) => {
            const topicProblems = topic.subTopics.flatMap(st => st.problems);
            const topicCompleted = topicProblems.filter(p => completedProblems.includes(p.id)).length;
            const topicTotal = topicProblems.length;
            const topicPercent = topicTotal > 0 ? (topicCompleted/topicTotal)*100 : 0;
            const isTopicExpanded = expandedTopics[topic.id];

            return (
              <div key={topic.id} className="bg-white border border-slate-200 rounded-xl overflow-hidden shadow-sm">
                
                {/* Topic Header */}
                <button 
                  onClick={() => toggleTopic(topic.id)}
                  className="w-full flex items-center justify-between p-4 md:px-6 md:py-5 bg-white hover:bg-slate-50 transition-colors border-b border-slate-100"
                >
                  <div className="flex items-center gap-3">
                    <ChevronRight className={`w-5 h-5 text-slate-500 transition-transform duration-200 ${isTopicExpanded ? 'rotate-90' : ''}`} />
                    <h2 className="text-lg md:text-xl font-bold text-slate-900 text-left">{topic.title}</h2>
                  </div>
                  <div className="flex items-center gap-4">
                    <div className="w-24 h-1.5 bg-slate-200 rounded-full overflow-hidden hidden sm:block">
                      <div className="h-full bg-orange-500 rounded-full transition-all duration-500" style={{ width: `${topicPercent}%` }}></div>
                    </div>
                    <span className="text-slate-500 font-bold text-sm tabular-nums w-12 text-right">{topicCompleted} / {topicTotal}</span>
                  </div>
                </button>

                {/* SubTopics */}
                {isTopicExpanded && (
                  <div className="p-4 md:p-6 space-y-4 bg-slate-50/50">
                    {topic.subTopics.map((subTopic) => {
                      const subCompleted = subTopic.problems.filter(p => completedProblems.includes(p.id)).length;
                      const subTotal = subTopic.problems.length;
                      const subPercent = subTotal > 0 ? (subCompleted/subTotal)*100 : 0;
                      const isSubExpanded = expandedSubTopics[subTopic.id];

                      return (
                        <div key={subTopic.id} className="border border-slate-200 rounded-xl overflow-hidden shadow-sm bg-white">
                          <button 
                            onClick={() => toggleSubTopic(subTopic.id)}
                            className="w-full flex items-center justify-between p-4 bg-white hover:bg-slate-50 transition-colors"
                          >
                            <div className="flex items-center gap-3 pl-2">
                              <ChevronRight className={`w-4 h-4 text-slate-400 transition-transform duration-200 ${isSubExpanded ? 'rotate-90' : ''}`} />
                              <h3 className="text-base font-bold text-slate-800 text-left">{subTopic.title}</h3>
                            </div>
                            <div className="flex items-center gap-4 pr-2">
                              <div className="w-16 h-1.5 bg-slate-200 rounded-full overflow-hidden hidden sm:block">
                                <div className="h-full bg-orange-500 rounded-full transition-all duration-500" style={{ width: `${subPercent}%` }}></div>
                              </div>
                              <span className="text-slate-500 font-bold text-sm tabular-nums w-12 text-right">{subCompleted} / {subTotal}</span>
                            </div>
                          </button>

                          {/* Problems Table */}
                          {isSubExpanded && (
                            <div className="overflow-x-auto border-t border-slate-200">
                              <table className="w-full text-left border-collapse min-w-[800px]">
                                <thead>
                                  <tr className="bg-slate-50 border-b border-slate-200 text-xs uppercase tracking-wider text-slate-700 font-bold">
                                    <th className="px-6 py-4 text-center w-16">Status</th>
                                    <th className="px-6 py-4">Problem</th>
                                    <th className="px-6 py-4 text-center w-32">Resource</th>
                                    <th className="px-6 py-4 text-center w-28">Practice</th>
                                    <th className="px-6 py-4 text-center w-20">Note</th>
                                    <th className="px-6 py-4 text-center w-20">Revision</th>
                                    <th className="px-6 py-4 text-center w-28">Difficulty</th>
                                  </tr>
                                </thead>
                                <tbody className="divide-y divide-slate-100 bg-white">
                                  {subTopic.problems.map((problem) => {
                                    const isDone = completedProblems.includes(problem.id);
                                    const isStarred = starredProblems.includes(problem.id);

                                    let diffBg = "bg-slate-100 text-slate-600 border-slate-200";
                                    if (problem.difficulty === 'Medium') diffBg = "bg-amber-50 text-amber-600 border-amber-200";
                                    if (problem.difficulty === 'Hard') diffBg = "bg-rose-50 text-rose-600 border-rose-200";
                                    if (problem.difficulty === 'Easy') diffBg = "bg-emerald-50 text-emerald-600 border-emerald-200";

                                    return (
                                      <tr key={problem.id} className="hover:bg-slate-50 transition-colors group">
                                        {/* Status */}
                                        <td className="px-6 py-4 text-center">
                                          <button 
                                            onClick={() => toggleCompletion(problem.id)}
                                            className={`w-5 h-5 rounded flex items-center justify-center mx-auto transition-colors ${isDone ? 'bg-orange-500 border-orange-500 text-white' : 'bg-white border-2 border-slate-300 hover:border-orange-400'}`}
                                          >
                                            {isDone && <CheckCircle2 className="w-3.5 h-3.5" />}
                                          </button>
                                        </td>
                                        
                                        {/* Problem */}
                                        <td className="px-6 py-4">
                                          <span className={`font-bold text-sm ${isDone ? 'text-slate-400 line-through' : 'text-slate-800'}`}>
                                            {problem.title}
                                          </span>
                                        </td>

                                        {/* Resource */}
                                        <td className="px-6 py-4 text-center">
                                          <div className="flex items-center justify-center gap-3">
                                            {problem.videoResource && problem.videoResource.length > 0 && problem.videoResource.map((link, idx) => (
                                              <a key={idx} href={link} target="_blank" rel="noreferrer" className="text-rose-500 hover:text-rose-600" title={`Video ${idx + 1}`}>
                                                <PlaySquare className="w-5 h-5" />
                                              </a>
                                            ))}
                                            {problem.documentationResource && (
                                              <a href={problem.documentationResource} target="_blank" rel="noreferrer" className="text-blue-500 hover:text-blue-600" title="Documentation">
                                                <FileText className="w-5 h-5" />
                                              </a>
                                            )}
                                            {(!problem.videoResource || problem.videoResource.length === 0) && !problem.documentationResource && <span className="text-slate-300 font-bold">---</span>}
                                          </div>
                                        </td>

                                        {/* Practice */}
                                        <td className="px-6 py-4 text-center">
                                          {problem.practiceLink || problem.documentationResource ? (
                                            <a href={problem.practiceLink || problem.documentationResource} target="_blank" rel="noreferrer" className="text-orange-500 font-bold text-sm hover:underline">
                                              Solve
                                            </a>
                                          ) : (
                                            <span className="text-slate-300 font-bold">---</span>
                                          )}
                                        </td>

                                        {/* Note */}
                                        <td className="px-6 py-4 text-center">
                                          <span className="text-slate-300 font-bold">---</span>
                                        </td>

                                        {/* Revision */}
                                        <td className="px-6 py-4 text-center">
                                          <button 
                                            onClick={() => toggleStar(problem.id)}
                                            className={`transition-colors ${isStarred ? 'text-amber-500' : 'text-slate-300 hover:text-amber-400'}`}
                                          >
                                            <Star className="w-5 h-5" fill={isStarred ? "currentColor" : "none"} />
                                          </button>
                                        </td>

                                        {/* Difficulty */}
                                        <td className="px-6 py-4 text-center">
                                          <span className={`inline-flex items-center justify-center px-3 py-1 rounded-full text-xs font-bold border ${diffBg}`}>
                                            {problem.difficulty}
                                          </span>
                                        </td>
                                      </tr>
                                    );
                                  })}
                                </tbody>
                              </table>
                            </div>
                          )}
                        </div>
                      );
                    })}
                  </div>
                )}
              </div>
            );
          })}
        </div>

      </div>
      
      <StreakModal 
        isOpen={showStreakModal} 
        onClose={() => setShowStreakModal(false)} 
        streakDays={currentStreak} 
      />
    </div>
  );
}
