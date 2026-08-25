import { LayoutDashboard, Award, Target, Lightbulb, TrendingUp, User } from 'lucide-react';
import { Link, useLocation } from 'react-router';

export function Sidebar() {
  const location = useLocation();

  const menuItems = [
    { id: 'dashboard', label: 'Dashboard', icon: LayoutDashboard, path: '/' },
    { id: 'skills', label: 'My Skills', icon: Award, path: '/skills' },
    { id: 'careers', label: 'Career Paths', icon: Target, path: '/careers' },
    { id: 'progress', label: 'Progress Tracker', icon: TrendingUp, path: '/progress' },
    { id: 'profile', label: 'My Profile', icon: User, path: '/profile' },
  ];

  return (
    <div className="w-64 bg-white border-r border-gray-200 h-screen flex flex-col">
      <div className="p-6 border-b border-gray-200">
        <h1 className="text-2xl font-bold text-blue-600">SkillRoute</h1>
        <p className="text-sm text-gray-500 mt-1">Career Guidance</p>
      </div>
      <nav className="flex-1 p-4">
        {menuItems.map((item) => {
          const Icon = item.icon;
          const isActive = location.pathname === item.path;
          return (
            <Link
              key={item.id}
              to={item.path}
              className={`w-full flex items-center gap-3 px-4 py-3 rounded-lg mb-2 transition-colors ${
                isActive
                  ? 'bg-blue-50 text-blue-600'
                  : 'text-gray-700 hover:bg-gray-50'
              }`}
            >
              <Icon size={20} />
              <span>{item.label}</span>
            </Link>
          );
        })}
      </nav>
    </div>
  );
}
