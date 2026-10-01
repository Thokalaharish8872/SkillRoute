import { useEffect, useState } from 'react';
import axios from 'axios';
import { RecommendedRoles } from '../components/RecommendedRoles';
import { Filter, Loader2, AlertTriangle, ServerCrash, RefreshCw } from 'lucide-react';
import { getUserIdFromToken } from '../utils/api';

type ErrorKind = 'ai_overload' | 'server' | 'network';

interface FetchError {
  kind: ErrorKind;
  message: string;
}

const classifyError = (err: unknown): FetchError => {
  if (axios.isAxiosError(err)) {
    const status = err.response?.status;
    if (status === 503 || status === 429) {
      return { kind: 'ai_overload', message: 'The AI is currently experiencing high demand. Please try again in a moment.' };
    }
    if (status && status >= 500) {
      return { kind: 'server', message: `Server error (${status}). Our backend ran into an issue. Please try again.` };
    }
    if (!err.response) {
      return { kind: 'network', message: 'Could not reach the server. Please check your connection and try again.' };
    }
  }
  return { kind: 'server', message: 'Something went wrong while loading career paths. Please try again.' };
};

export function CareersPage() {
  const [roles, setRoles] = useState<any[]>(() => {
    const saved = sessionStorage.getItem('recommendedRoles');
    if (saved) {
      try {
        const parsed = JSON.parse(saved);
        return Array.isArray(parsed) ? parsed : [];
      } catch {
        return [];
      }
    }
    return [];
  });
  const [loading, setLoading]     = useState(false);
  const [fetchError, setFetchError] = useState<FetchError | null>(null);

  useEffect(() => {
    if (roles.length === 0) {
      fetchRoles(false);
    }
  }, []);

  const fetchRoles = async (refresh: boolean) => {
    try {
      setLoading(true);
      setFetchError(null);

      const userId = getUserIdFromToken();
      const response = await axios.get(
        'http://localhost:8080/api/career_path/recommendations',
        { params: { userId: userId || undefined, refresh } }
      );

      const rolesData = response.data?.roles;
      if (!rolesData || (Array.isArray(rolesData) && rolesData.length === 0)) {
        setFetchError({
          kind: 'ai_overload',
          message: 'The AI is currently experiencing high demand and could not generate recommendations. Please try again in a moment.',
        });
        setRoles([]);
        return;
      }

      setRoles(rolesData);
      sessionStorage.setItem('recommendedRoles', JSON.stringify(response.data.paths));
    } catch (error) {
      console.error('Error fetching career path recommendations:', error);
      setFetchError(classifyError(error));
      setRoles([]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-8">
      <div className="max-w-7xl mx-auto space-y-6">

        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold text-gray-900 mb-2">Career Paths</h1>
            <p className="text-gray-600">Explore roles that match your skills</p>
          </div>

          <div className="flex gap-2">
            <button
              onClick={() => { sessionStorage.removeItem('recommendedRoles'); fetchRoles(true); }}
              className="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
            >
              Regenerate
            </button>
            <button className="flex items-center gap-2 px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50">
              <Filter size={20} />
              Filter
            </button>
          </div>
        </div>

        <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
          <p className="text-sm text-blue-800">
            <strong>Tip:</strong> Add more skills to your profile to get better career matches!
          </p>
        </div>

        {loading ? (
          <div className="flex flex-col items-center justify-center py-20 gap-4">
            <Loader2 className="w-12 h-12 text-blue-600 animate-spin" />
            <p className="text-gray-500 font-medium">Analyzing your skills and generating recommendations…</p>
          </div>
        ) : fetchError ? (
          <div className="flex flex-col items-center justify-center py-20 px-8 gap-6 text-center">
            <div className={`flex flex-col items-center gap-4 max-w-md p-8 rounded-2xl border ${
              fetchError.kind === 'ai_overload' ? 'bg-yellow-50 border-yellow-200' : 'bg-red-50 border-red-200'
            }`}>
              {fetchError.kind === 'ai_overload'
                ? <AlertTriangle className="w-8 h-8 text-yellow-500" />
                : <ServerCrash   className="w-8 h-8 text-red-500" />
              }
              <div>
                <h2 className="text-lg font-bold text-gray-800 mb-1">
                  {fetchError.kind === 'ai_overload' ? '⚡ AI Overloaded — Try Again Later' : '⚠️ Failed to Load Recommendations'}
                </h2>
                <p className="text-sm text-gray-600">{fetchError.message}</p>
              </div>
              <button
                onClick={() => fetchRoles(false)}
                className="flex items-center gap-2 px-5 py-2.5 bg-slate-900 text-white rounded-lg text-sm font-semibold hover:bg-slate-800 transition-colors"
              >
                <RefreshCw className="w-4 h-4" />
                Retry
              </button>
            </div>
          </div>
        ) : (
          <RecommendedRoles roles={roles} />
        )}

      </div>
    </div>
  );
}