import { TrendingUp, Target, Award, BookOpen, Flame, Loader2 } from 'lucide-react';
import { Link } from 'react-router';
import { useState, useEffect } from 'react';
import axios from 'axios';

interface Activity {
  title: string;
  createdAt: string;
  type?: string;
}

export function DashboardPage() {
  const [streak, setStreak] = useState<number | string>('-');
  const [recentActivity, setRecentActivity] = useState<Activity[]>([]);
  const [isLoadingActivity, setIsLoadingActivity] = useState(true);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        // Fetch Streak
        const streakRes = await axios.get('http://localhost:8080/api/streak/get_streak?userId=1');
        setStreak(streakRes.data.streak !== undefined ? streakRes.data.streak : (streakRes.data || 0));
        
        // Fetch Recent Activity
        const activityRes = await axios.get('http://localhost:8080/api/activity/get_recent_activity?userId=1');
        setRecentActivity(Array.isArray(activityRes.data.recentActivities) ? activityRes.data.recentActivities : []);
      } catch (error) {
        console.error('Failed to fetch dashboard data:', error);
      } finally {
        setIsLoadingActivity(false);
      }
    };
    
    fetchDashboardData();
  }, []);

  const stats = [
    { label: 'Day Streak', value: `${streak}`, icon: Flame, color: 'bg-orange-500', link: '#' },
    { label: 'Skills Mastered', value: '5', icon: Award, color: 'bg-blue-500', link: '/skills' },
    { label: 'Career Matches', value: '4', icon: Target, color: 'bg-green-500', link: '/careers' },
  ];

  return (
    <div className="p-8">
      <div className="max-w-7xl mx-auto space-y-6">
        <div>
          <h1 className="text-3xl font-bold text-gray-900 mb-2">Welcome back!</h1>
          <p className="text-gray-600">Here's your learning progress overview</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
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
                <p className="text-3xl font-bold text-gray-900 mb-1">{stat.value}</p>
                <p className="text-sm text-gray-600">{stat.label}</p>
              </Link>
            );
          })}
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
            <h2 className="text-xl font-semibold text-gray-800 mb-4">Recent Activity</h2>
            
            {isLoadingActivity ? (
              <div className="flex flex-col items-center justify-center py-8 text-gray-400">
                <Loader2 className="w-8 h-8 animate-spin mb-2 text-blue-500" />
                <p className="text-sm">Loading activity...</p>
              </div>
            ) : recentActivity.length > 0 ? (
              <div className="space-y-4 max-h-[300px] overflow-y-auto pr-2">
                {recentActivity.map((activity, index) => (
                  <div key={index} className="flex items-start gap-3 pb-4 border-b border-gray-100 last:border-0 hover:bg-gray-50 transition-colors p-2 rounded-lg -mx-2">
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
