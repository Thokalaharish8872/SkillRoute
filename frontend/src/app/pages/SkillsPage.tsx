import { useEffect, useState } from 'react';
import { SkillInput } from '../components/SkillInput';
import { SkillGaps } from '../components/SkillGaps';
import axios from 'axios';

export function SkillsPage() {
  const [userSkills, setUserSkills] = useState<string[]>([]); 
  const mockGaps = [
    {
      skill: 'TypeScript',
      priority: 'high',
      relevantFor: ['Frontend Developer', 'Full Stack Developer'],
    },
    {
      skill: 'Node.js',
      priority: 'high',
      relevantFor: ['Full Stack Developer', 'Backend Developer'],
    },
    {
      skill: 'SQL/Database',
      priority: 'medium',
      relevantFor: ['Backend Developer', 'Full Stack Developer'],
    },
    {
      skill: 'REST APIs',
      priority: 'medium',
      relevantFor: ['Backend Developer', 'Full Stack Developer'],
    },
  ];

  useEffect(() => {
    const fetchSkills = async () => {
        try {
            const response = await axios.get(
                "http://localhost:8080/api/skills/get_user_skills",
                {
                    params: {
                        userId: 1
                    }
                }
            );

            console.log(response.data);
            setUserSkills(response.data.skills);
        } catch(error) {
            console.error(error);
        }
    };

    fetchSkills();
  }, []);

  return (
    <div className="p-8">
      <div className="max-w-7xl mx-auto space-y-6">
        <div>
          <h1 className="text-3xl font-bold text-gray-900 mb-2">My Skills</h1>
          <p className="text-gray-600">Manage your skills and identify areas for growth</p>
        </div>

        <SkillInput 
          skills={userSkills} 
          onSkillsChange={setUserSkills} 
          />

        <SkillGaps gaps={mockGaps} />

        <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <h2 className="text-xl font-semibold text-gray-800 mb-4">Skill Categories</h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div className="p-4 border border-gray-200 rounded-lg">
              <h3 className="font-semibold text-gray-800 mb-2">Frontend</h3>
              <div className="flex flex-wrap gap-2">
                {userSkills.filter(s => ['React', 'CSS', 'HTML', 'JavaScript'].includes(s)).map(skill => (
                  <span key={skill} className="px-2 py-1 bg-blue-50 text-blue-700 rounded text-sm">
                    {skill}
                  </span>
                ))}
              </div>
            </div>
            <div className="p-4 border border-gray-200 rounded-lg">
              <h3 className="font-semibold text-gray-800 mb-2">Backend</h3>
              <div className="flex flex-wrap gap-2">
                <span className="px-2 py-1 bg-gray-100 text-gray-500 rounded text-sm">No skills yet</span>
              </div>
            </div>
            <div className="p-4 border border-gray-200 rounded-lg">
              <h3 className="font-semibold text-gray-800 mb-2">Tools</h3>
              <div className="flex flex-wrap gap-2">
                {userSkills.filter(s => ['Git'].includes(s)).map(skill => (
                  <span key={skill} className="px-2 py-1 bg-green-50 text-green-700 rounded text-sm">
                    {skill}
                  </span>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

