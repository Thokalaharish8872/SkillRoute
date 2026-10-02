import { TrendingUp, Target, Award, Flame, Loader2 } from 'lucide-react';
import { Link } from 'react-router';
import { useState, useEffect } from 'react';
import axios from 'axios';
import { getUserIdFromToken, API_BASE_URL } from '../utils/api';

interface Activity {
  id?: number;
  activityType?: string;
  title: string;
  skillName?: string;
  createdAt: string;
}

interface DashboardData {
  userId?: number;
  streak: number;
  skillsMasteredCount: number;
  careerMatchesCount: number;
  recentActivities: Activity[];
}

export function DashboardPage() {
  const [dashboardData, setDashboardData] = useState<DashboardData | null>(null);
  const [isLoading, setIsLoading]         = useState(true);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        setIsLoading(true);
        const userId = getUserIdFromToken();
        const response = await axios.get<DashboardData>(
          `${API_BASE_URL}/api/dashboard/get_dashboard_data`,
          {
            params: userId ? { userId } : {},
          }
        );
        setDashboardData(response.data);
      } catch (error) {
        console.error('Failed to fetch dashboard data:', error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchDashboardData();
  }, []);

  const streak              = dashboardData?.streak ?? 0;
  const skillsMasteredCount = dashboardData?.skillsMasteredCount ?? 0;
  const careerMatchesCount  = dashboardData?.careerMatchesCount ?? 0;
  const recentActivities    = dashboardData?.recentActivities ?? [];

  const stats = [
    { label: 'Day Streak',       value: `${streak}`,              icon: Flame,  color: 'bg-orange-500', link: '/progress' },
    { label: 'Skills Mastered',  value: `${skillsMasteredCount}`, icon: Award,  color: 'bg-blue-500',   link: '/skills'   },
    { label: 'Career Matches',   value: `${careerMatchesCount}`,  icon: Target, color: 'bg-green-500',  link: '/careers'  },
  ];

  return (
    <div className="p-8">
      <div className="max-w-7xl mx-auto space-y-6">
        <div>
          <h1 className="text-3xl font-bold text-gray-900 mb-2">Welcome back!</h1>
          <p className="text-gray-600">Here's your learning progress overview</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {stats.map((stat) => {
            const Icon = stat.icon;
            return (
              <Link
                key={stat.label}
                to={stat.link}
                className="bg-white rounded-xl shadow-sm border border-gray-200 p-6 hover:shadow-md transition-shadow"
              >
                <div className="flex items-center justify-between mb-4">
                  <div className={`w-12 h-12 ${stat.color} rounded-lg flex items-center justify-center`}>
                    <Icon className="text-white" size={24} />
                  </div>
                </div>
                <p className="text-3xl font-bold text-gray-900 mb-1">
                  {isLoading ? <Loader2 className="w-6 h-6 animate-spin text-gray-400" /> : stat.value}
                </p>
                <p className="text-sm text-gray-600">{stat.label}</p>
              </Link>
            );
          })}
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
            <h2 className="text-xl font-semibold text-gray-800 mb-4">Recent Activity</h2>

            {isLoading ? (
              <div className="flex flex-col items-center justify-center py-8 text-gray-400">
                <Loader2 className="w-8 h-8 animate-spin mb-2 text-blue-500" />
                <p className="text-sm">Loading activity...</p>
              </div>
            ) : recentActivities.length > 0 ? (
              <div className="space-y-4 max-h-[300px] overflow-y-auto pr-2">
                {recentActivities.map((activity, index) => (
                  <div
                    key={activity.id || index}
                    className="flex items-start gap-3 pb-4 border-b border-gray-100 last:border-0 hover:bg-gray-50 transition-colors p-2 rounded-lg -mx-2"
                  >
                    <div className="w-2.5 h-2.5 bg-blue-500 rounded-full mt-1.5 shadow-sm shadow-blue-200"></div>
                    <div className="flex-1">
                      <p className="text-gray-800 font-medium">{activity.title}</p>
                      <p className="text-xs text-gray-500 mt-1">{activity.createdAt}</p>
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <div className="flex flex-col items-center justify-center py-8 text-gray-400 text-center">
                <div className="w-12 h-12 bg-gray-50 rounded-full flex items-center justify-center mb-3">
                  <TrendingUp className="w-6 h-6 text-gray-300" />
                </div>
                <p className="text-gray-500 font-medium">No recent activity yet</p>
                <p className="text-sm mt-1">Start learning to see your progress here!</p>
              </div>
            )}
          </div>

          <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
            <h2 className="text-xl font-semibold text-gray-800 mb-4">Quick Actions</h2>
            <div className="space-y-3">
              <Link
                to="/skills"
                className="block w-full p-4 border border-gray-200 rounded-lg hover:border-blue-300 hover:bg-blue-50 transition-colors"
              >
                <h3 className="font-semibold text-gray-800 mb-1">Add New Skills</h3>
                <p className="text-sm text-gray-600">Update your skill profile</p>
              </Link>
              <Link
                to="/careers"
                className="block w-full p-4 border border-gray-200 rounded-lg hover:border-blue-300 hover:bg-blue-50 transition-colors"
              >
                <h3 className="font-semibold text-gray-800 mb-1">Explore Careers</h3>
                <p className="text-sm text-gray-600">Find matching career paths</p>
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
