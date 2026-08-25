import { LearningRecommendations } from '../components/LearningRecommendations';
import { Filter } from 'lucide-react';

export function RecommendationsPage() {
  const mockCourses = [
    {
      title: 'TypeScript Complete Guide',
      platform: 'Udemy',
      difficulty: 'Beginner',
      duration: '12 hours',
      skill: 'TypeScript',
    },
    {
      title: 'Node.js Mastery',
      platform: 'Coursera',
      difficulty: 'Intermediate',
      duration: '8 weeks',
      skill: 'Node.js',
    },
    {
      title: 'SQL for Developers',
      platform: 'YouTube',
      difficulty: 'Beginner',
      duration: '4 hours',
      skill: 'SQL',
    },
    {
      title: 'RESTful API Design',
      platform: 'Udemy',
      difficulty: 'Intermediate',
      duration: '10 hours',
      skill: 'REST APIs',
    },
    {
      title: 'Advanced React Patterns',
      platform: 'Coursera',
      difficulty: 'Advanced',
      duration: '6 weeks',
      skill: 'React',
    },
    {
      title: 'Modern JavaScript ES6+',
      platform: 'YouTube',
      difficulty: 'Intermediate',
      duration: '5 hours',
      skill: 'JavaScript',
    },
    {
      title: 'Docker for Beginners',
      platform: 'Udemy',
      difficulty: 'Beginner',
      duration: '8 hours',
      skill: 'Docker',
    },
    {
      title: 'AWS Cloud Practitioner',
      platform: 'Coursera',
      difficulty: 'Beginner',
      duration: '10 weeks',
      skill: 'AWS',
    },
    {
      title: 'GraphQL Complete Guide',
      platform: 'YouTube',
      difficulty: 'Intermediate',
      duration: '6 hours',
      skill: 'GraphQL',
    },
  ];

  return (
    <div className="p-8">
      <div className="max-w-7xl mx-auto space-y-6">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold text-gray-900 mb-2">Learning Recommendations</h1>
            <p className="text-gray-600">Curated courses to fill your skill gaps</p>
          </div>
          <button className="flex items-center gap-2 px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50">
            <Filter size={20} />
            Filter
          </button>
        </div>

        <div className="flex gap-2">
          <button className="px-4 py-2 bg-blue-600 text-white rounded-lg">All</button>
          <button className="px-4 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200">Beginner</button>
          <button className="px-4 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200">Intermediate</button>
          <button className="px-4 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200">Advanced</button>
        </div>

        <LearningRecommendations courses={mockCourses} />
      </div>
    </div>
  );
}
