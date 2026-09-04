import { BookOpen, Youtube, GraduationCap } from 'lucide-react';

type Platform = 'YouTube' | 'Coursera' | 'Udemy';
type Difficulty = 'Beginner' | 'Intermediate' | 'Advanced';

interface Course {
  platform: Platform;
  title: string;
  difficulty: Difficulty;
  duration: string;
  skill: string;
}

interface LearningRecommendationsProps {
  courses: Course[];
}


export function LearningRecommendations({ courses }: LearningRecommendationsProps) {
  const platformIcons: Record<Platform, typeof BookOpen> = {
    YouTube: Youtube,
    Coursera: GraduationCap,
    Udemy: BookOpen,
  };

  const difficultyColors: Record<Difficulty, string> = {
    Beginner: 'bg-green-100 text-green-700',
    Intermediate: 'bg-yellow-100 text-yellow-700',
    Advanced: 'bg-red-100 text-red-700',
  };

  return (
    <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
      <h2 className="text-xl font-semibold text-gray-800 mb-4">Learning Recommendations</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {courses.map((course, index) => {
          const Icon = platformIcons[course.platform] || BookOpen;
          return (
            <div
              key={index}
              className="border border-gray-200 rounded-lg p-4 hover:border-blue-300 hover:shadow-md transition-all"
            >
              <div className="flex items-center gap-2 mb-3">
                <Icon className="text-blue-600" size={20} />
                <span className="text-sm text-gray-600">{course.platform}</span>
              </div>
              <h3 className="font-semibold text-gray-800 mb-2 line-clamp-2">{course.title}</h3>
              <div className="flex items-center gap-2 mb-2">
                <span className={`px-2 py-1 rounded text-xs font-semibold ${difficultyColors[course.difficulty]}`}>
                  {course.difficulty}
                </span>
                <span className="text-xs text-gray-500">{course.duration}</span>
              </div>
              <p className="text-sm text-gray-600 mb-3">Focus: {course.skill}</p>
              <button className="w-full py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors">
                Start Learning
              </button>
            </div>
          );
        })}
      </div>
    </div>
  );
}