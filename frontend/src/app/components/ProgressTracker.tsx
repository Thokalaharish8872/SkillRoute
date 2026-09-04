import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

import { useMemo } from 'react';

interface DailyActivity {
  activityId: number;
  userId: number;
  date: string;
  activeTime: number;
}

interface ProgressTrackerProps {
  dailyActivities: DailyActivity[];
}

export function ProgressTracker({ dailyActivities = [] }: ProgressTrackerProps) {
  console.log('ProgressTracker received dailyActivities:', dailyActivities);

  const weeklyData = useMemo(() => {
    // Take the last 7 days for the bar chart
    return dailyActivities.slice(-7).map(da => {
      const d = new Date(da.date);
      const dayName = isNaN(d.getTime()) ? da.date : d.toLocaleDateString('en-US', { weekday: 'short' });
      return {
        day: dayName,
        activeTime: da.activeTime,
      };
    });
  }, [dailyActivities]);

  const heatmapData = useMemo(() => {
    const today = new Date();
    const days = [];
    const activitiesMap = new Map(dailyActivities.map(da => [da.date, da.activeTime]));

    // Generate last 365 days ending today
    for (let i = 364; i >= 0; i--) {
      const d = new Date(today);
      d.setDate(today.getDate() - i);
      // Format as YYYY-MM-DD
      const dateStr = [
        d.getFullYear(),
        String(d.getMonth() + 1).padStart(2, '0'),
        String(d.getDate()).padStart(2, '0')
      ].join('-');
      
      const activeTime = activitiesMap.get(dateStr) || 0;
      days.push({
        date: dateStr,
        activeTime
      });
    }
    return days;
  }, [dailyActivities]);

  const totalActiveDays = dailyActivities.filter(da => da.activeTime > 0).length;

  return (
    <div className="space-y-6">
      <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
        <h2 className="text-xl font-semibold text-gray-800 mb-4">Weekly Activity</h2>
        <ResponsiveContainer width="100%" height={250}>
          <BarChart data={weeklyData}>
            <CartesianGrid strokeDasharray="3 3" stroke="#e5e7eb" />
            <XAxis dataKey="day" tick={{ fill: '#6b7280', fontSize: 12 }} />
            <YAxis
              tick={{ fill: '#6b7280', fontSize: 12 }}
              domain={[0, 'auto']}
              allowDataOverflow={false}
            />
            <Tooltip
              contentStyle={{
                backgroundColor: '#fff',
                border: '1px solid #e5e7eb',
                borderRadius: '8px',
              }}
              formatter={(value: number) => [`${value} hrs`, 'Active Time']}
            />
            {/* minPointSize ensures even tiny values show a visible bar */}
            <Bar dataKey="activeTime" fill="#2563eb" radius={[8, 8, 0, 0]} minPointSize={3} />
          </BarChart>
        </ResponsiveContainer>
      </div>

      <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6 overflow-hidden">
        <div className="flex flex-col sm:flex-row sm:justify-between sm:items-end mb-6 gap-2">
          <div>
            <h2 className="text-xl font-semibold text-gray-800 mb-1">Activity Heatmap</h2>
            <p className="text-gray-500 text-sm">
              <span className="text-gray-900 font-bold">246</span> submissions in the past one year
            </p>
          </div>
          <div className="flex gap-4 text-sm text-gray-500">
            <div>Total active days: <span className="text-gray-900 font-semibold">{totalActiveDays}</span></div>
            <div>Total recorded days: <span className="text-gray-900 font-semibold">{dailyActivities.length}</span></div>
          </div>
        </div>
        
        <div className="overflow-x-auto pb-4 custom-scrollbar">
          <div className="min-w-[800px]">
            {/* Months Row (Approximate) */}
            <div className="flex text-xs text-gray-400 mb-2 ml-8 justify-between pr-4">
              <span>Jan</span><span>Feb</span><span>Mar</span><span>Apr</span><span>May</span><span>Jun</span><span>Jul</span><span>Aug</span><span>Sep</span><span>Oct</span><span>Nov</span><span>Dec</span>
            </div>
            
            <div className="flex gap-2">
              {/* Days of week */}
              <div className="flex flex-col gap-[3px] text-[10px] text-gray-400 justify-around py-1 pr-1 w-6">
                <span className="mt-1">Mon</span>
                <span>Wed</span>
                <span className="mb-1">Fri</span>
              </div>
              
              {/* Grid */}
              <div 
                className="grid gap-[3px] flex-1" 
                style={{ 
                  gridTemplateRows: 'repeat(7, 1fr)', 
                  gridAutoFlow: 'column',
                  gridAutoColumns: '12px' 
                }}
              >
                {heatmapData.map((day, i) => {
                  let intensity = 0;
                  if (day.activeTime > 0) intensity = 1;
                  if (day.activeTime > 1) intensity = 2;
                  if (day.activeTime > 3) intensity = 3;
                  if (day.activeTime > 5) intensity = 4;
                  
                  // Blue theme scale (0 is empty/ash color)
                  const colors = [
                    'bg-[#ebedf0]', // level 0 (empty)
                    'bg-blue-200',  // level 1
                    'bg-blue-400',  // level 2
                    'bg-blue-600',  // level 3
                    'bg-blue-800'   // level 4
                  ];
                  return (
                    <div
                      key={day.date}
                      className={`w-[12px] h-[12px] rounded-sm ${colors[intensity]} hover:ring-2 hover:ring-gray-300 transition-all cursor-pointer`}
                      title={`${day.date}: ${day.activeTime > 0 ? day.activeTime + ' hrs' : 'No activity'}`}
                    ></div>
                  );
                })}
              </div>
            </div>
          </div>
        </div>
        
        {/* Legend */}
        <div className="flex justify-end items-center gap-2 mt-4 text-xs text-gray-500">
          <span>Less</span>
          <div className="flex gap-[3px]">
            <div className="w-[12px] h-[12px] bg-[#ebedf0] rounded-sm"></div>
            <div className="w-[12px] h-[12px] bg-blue-200 rounded-sm"></div>
            <div className="w-[12px] h-[12px] bg-blue-400 rounded-sm"></div>
            <div className="w-[12px] h-[12px] bg-blue-600 rounded-sm"></div>
            <div className="w-[12px] h-[12px] bg-blue-800 rounded-sm"></div>
          </div>
          <span>More</span>
        </div>
      </div>
    </div>
  );
}