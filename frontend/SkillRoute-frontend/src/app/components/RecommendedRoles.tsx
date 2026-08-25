import { Briefcase, TrendingUp } from 'lucide-react';
import { Link } from 'react-router';
interface Role {
  id: number;
  title: string;
  matchScore : number;
  salaryRange: string;
  growth: string;
  skillsRequired: string[];
}


interface RecommendedRolesProps {
  roles: Role[];
}



export function RecommendedRoles({ roles }: RecommendedRolesProps) {
  console.log(roles[0]?.title);
  return (
    <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
      <h2 className="text-xl font-semibold text-gray-800 mb-4">Recommended Career Paths</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {roles?.map((role) => (
          
          <Link
            key={role.title}
            to={`/career-path?role=${encodeURIComponent(role.title)}&roleId=${role.id || ''}`}
            className="block border border-gray-200 rounded-lg p-4 hover:border-blue-300 hover:shadow-md transition-all cursor-pointer bg-white text-left"
          >
            <div className="flex items-start justify-between mb-3">
              <div className="flex items-center gap-3">
                <div className="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center">
                  <Briefcase className="text-blue-600" size={24} />
                </div>
                <div>
                  <h3 className="font-semibold text-gray-800">{role.title}</h3>
                  <p className="text-sm text-gray-500">{role.salaryRange}</p>
                </div>
              </div>
            </div>
            <div className="mb-3">
              <div className="flex items-center justify-between mb-1">
                <span className="text-sm text-gray-600">Match Score</span>
                <span className="text-sm font-semibold text-blue-600">{role.matchScore}%</span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-2">
                <div
                  className="bg-blue-600 h-2 rounded-full"
                  style={{ width: `${role.matchScore}%` }}
                ></div>
              </div>
            </div>
            <div className="flex items-center gap-1 text-sm text-green-600">
              <TrendingUp size={16} />
              <span>{role.growth} growth</span>
            </div>
          </Link>
        ))}
      </div>
    </div>
  );
}