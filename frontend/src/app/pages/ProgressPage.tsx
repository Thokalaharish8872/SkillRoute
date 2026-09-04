import { ProgressTracker } from '../components/ProgressTracker';
import { Calendar, Loader2 } from 'lucide-react';
import { useState, useEffect } from 'react';
import axios from 'axios';

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
    axios
      .get<ProgressResponse>('http://localhost:8080/api/progress/get_progress?userId=1')
      .then(res => {
        console.log('RAW API response:', JSON.stringify(res.data, null, 2));
        setData(res.data);
      })
      .catch(err => console.error('Failed to fetch progress:', err))
      .finally(() => setIsLoading(false));
  }, []);

  const dailyActivities = data?.dailyActivities ?? [];
  console.log('dailyActivities passed to chart:', dailyActivities);

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
                  {data ? `${data.totalActiveTime.toFixed(2)} hrs` : '—'}
                </p>
                <p className="text-sm text-gray-400 mt-2">all time</p>
              </div>
              <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
                <p className="text-sm text-gray-600 mb-1">Today's Learning Time</p>
                <p className="text-3xl font-bold text-gray-900">
                  {data ? `${data.todayActiveTime.toFixed(2)} hrs` : '—'}
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
