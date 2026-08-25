import { AlertCircle } from 'lucide-react';

export function SkillGaps({ gaps }) {
  const priorityColors = {
    high: 'bg-red-50 text-red-700 border-red-200',
    medium: 'bg-yellow-50 text-yellow-700 border-yellow-200',
    low: 'bg-green-50 text-green-700 border-green-200',
  };

  const priorityBadge = {
    high: 'bg-red-100 text-red-700',
    medium: 'bg-yellow-100 text-yellow-700',
    low: 'bg-green-100 text-green-700',
  };

  return (
    <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
      <div className="flex items-center gap-2 mb-4">
        <AlertCircle className="text-orange-500" size={24} />
        <h2 className="text-xl font-semibold text-gray-800">Skill Gaps</h2>
      </div>
      <div className="space-y-3">
        {gaps.map((gap) => (
          <div
            key={gap.skill}
            className={`border rounded-lg p-4 ${priorityColors[gap.priority]}`}
          >
            <div className="flex items-center justify-between mb-2">
              <h3 className="font-semibold">{gap.skill}</h3>
              <span className={`px-3 py-1 rounded-full text-xs font-semibold ${priorityBadge[gap.priority]}`}>
                {gap.priority.toUpperCase()}
              </span>
            </div>
            <p className="text-sm">
              Needed for: {gap.relevantFor.join(', ')}
            </p>
          </div>
        ))}
      </div>
    </div>
  );
}
