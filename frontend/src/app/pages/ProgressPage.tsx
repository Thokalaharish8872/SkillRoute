import { ProgressTracker } from '../components/ProgressTracker';
import { Calendar, Loader2 } from 'lucide-react';
import { useState, useEffect } from 'react';
import axios from 'axios';
import { getUserIdFromToken } from '../utils/api';
import { SESSION_START_KEY } from '../layouts/RootLayout';

// Key storing the last time active-time was flushed to the server
const LAST_SYNC_KEY = 'sr_last_sync';

interface DailyActivity {
  activityId: number;
  userId: number;
  date: string;
  activeTime: number;
}

interface ProgressResponse {
  userId: number;
  totalActiveTime: number;
  todayActiveTime: number;
  dailyActivities: DailyActivity[];
  coursesCompleted: number;
  streak: number;
}

export function ProgressPage() {
  const [data, setData]           = useState<ProgressResponse | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const flushAndFetch = async () => {
      // ── 1. Flush accumulated active time ──────────────────────────────
      try {
        const lastSync  = Number(sessionStorage.getItem(LAST_SYNC_KEY) || 0);
        const sessionStart = Number(sessionStorage.getItem(SESSION_START_KEY) || Date.now());
        // Compute elapsed since last sync (or session start if never synced)
        const referenceTime = lastSync > 0 ? lastSync : sessionStart;
        const durationSeconds = Math.floor((Date.now() - referenceTime) / 1000);

        if (durationSeconds >= 5) {
          const userId = getUserIdFromToken();
          const token  = localStorage.getItem('token');
          await fetch('http://localhost:8080/api/progress/update_active_time', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              ...(token ? { Authorization: `Bearer ${token}` } : {}),
            },
            body: JSON.stringify({
              userId: userId || undefined,
              durationSeconds,
            }),
          });
          // Record the flush time
          sessionStorage.setItem(LAST_SYNC_KEY, String(Date.now()));
          console.log(`Flushed ${durationSeconds}s of active time before fetching progress`);
        }
      } catch (err) {
        console.error('Failed to flush active time:', err);
      }

      // ── 2. Fetch updated progress ──────────────────────────────────────
      try {
        const userId = getUserIdFromToken();
        const res = await axios.get<ProgressResponse>(
          'http://localhost:8080/api/progress/get_progress',
          { params: userId ? { userId } : {} }
        );
        console.log('RAW API response:', JSON.stringify(res.data, null, 2));
        setData(res.data);
      } catch (err) {
        console.error('Failed to fetch progress:', err);
      } finally {
        setIsLoading(false);
      }
    };

    flushAndFetch();
  }, []);

  const dailyActivities = data?.dailyActivities ?? [];

  return (
    <div className="p-8">
      <div className="max-w-7xl mx-auto space-y-6">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold text-gray-900 mb-2">Progress Tracker</h1>
            <p className="text-gray-600">Monitor your learning journey and achievements</p>
          </div>
          <button className="flex items-center gap-2 px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50">
            <Calendar size={20} />
            This Week
          </button>
        </div>

        {isLoading ? (
          <div className="flex flex-col items-center justify-center py-20 text-blue-600">
            <Loader2 className="w-10 h-10 animate-spin mb-4" />
            <p className="text-gray-500 font-medium">Loading progress...</p>
          </div>
        ) : (
          <>
            <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
              <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
                <p className="text-sm text-gray-600 mb-1">Total Learning Time</p>
                <p className="text-3xl font-bold text-gray-900">
                  {data ? `${(data.totalActiveTime ?? 0).toFixed(2)} hrs` : '—'}
                </p>
                <p className="text-sm text-gray-400 mt-2">all time</p>
              </div>
              <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
                <p className="text-sm text-gray-600 mb-1">Today's Learning Time</p>
                <p className="text-3xl font-bold text-gray-900">
                  {data ? `${(data.todayActiveTime ?? 0).toFixed(2)} hrs` : '—'}
                </p>
                <p className="text-sm text-gray-400 mt-2">today</p>
              </div>
              <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
                <p className="text-sm text-gray-600 mb-1">Courses Completed</p>
                <p className="text-3xl font-bold text-gray-900">
                  {data?.coursesCompleted ?? '—'}
                </p>
                <p className="text-sm text-gray-400 mt-2">total</p>
              </div>
              <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
                <p className="text-sm text-gray-600 mb-1">Current Streak</p>
                <p className="text-3xl font-bold text-gray-900">
                  {data ? `${data.streak} days` : '—'}
                </p>
                <p className="text-sm text-orange-600 mt-2">Keep it up!</p>
              </div>
            </div>

            <ProgressTracker dailyActivities={dailyActivities} />
          </>
        )}
      </div>
    </div>
  );
}
