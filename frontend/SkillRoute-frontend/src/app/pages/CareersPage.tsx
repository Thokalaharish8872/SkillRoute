import { useEffect, useState } from 'react';
import axios from 'axios';
import { RecommendedRoles } from '../components/RecommendedRoles';
import { Filter, Loader2 } from 'lucide-react';

export function CareersPage() {

  console.log('Rendering CareersPage');
  const [roles, setRoles] = useState(() => {
  const saved = sessionStorage.getItem('recommendedRoles');

  console.log('Loaded recommended roles from sessionStorage:', saved);
  if (saved) {
    try {
      const parsed = JSON.parse(saved);
      console.log(parsed[0]?.roleName);
      return Array.isArray(parsed) ? parsed : [];
    } catch {
      console.error('Failed to parse recommended roles from sessionStorage');
      return [];
    }
  }

  return [];
});
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (roles.length === 0) {
      fetchRoles();
    }
  }, []);

  const fetchRoles = async () => {
    try {
      setLoading(true);

      console.log('Fetching career path recommendations from API...');
      const response = await axios.get(
        'http://localhost:8080/api/career_path/recommendations',
        {
          params: {
            userId: 1
          }
        }
      );

      console.log('Received career path recommendations:', response.data);
      setRoles(response.data.roles);
      sessionStorage.setItem('recommendedRoles', JSON.stringify(response.data.paths));
    } catch (error) {
      console.error('Error fetching career path recommendations:', error);
      console.error(error);
    } finally {

      console.log('Finished fetching career path recommendations');
      setLoading(false);
    }
  };

  return (
    <div className="p-8">
      <div className="max-w-7xl mx-auto space-y-6">

        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold text-gray-900 mb-2">
              Career Paths
            </h1>
            <p className="text-gray-600">
              Explore roles that match your skills
            </p>
          </div>

          <div className="flex gap-2">
            <button 
              onClick={() => {
                sessionStorage.removeItem('recommendedRoles');
                fetchRoles();
              }}
              className="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
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
            <p className="text-gray-500 font-medium">Analyzing your skills and generating recommendations...</p>
          </div>
        ) : (
          <RecommendedRoles roles={roles} />
        )}

      </div>
    </div>
  );
}