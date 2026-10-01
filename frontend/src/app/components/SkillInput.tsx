import { ChangeEvent, KeyboardEvent, useState } from 'react';
import axios from "axios";
import { X, Plus } from 'lucide-react';
import { getUserIdFromToken } from '../utils/api';

interface SkillInputProps {
  skills: string[];
  onSkillsChange: (updatedSkills: string[]) => void;
}

export function SkillInput({ skills, onSkillsChange }: SkillInputProps) {
  const [inputValue, setInputValue] = useState('');

  const addSkill = async () => {
    if (inputValue.trim() && !skills.includes(inputValue.trim())) {
      const newSkill = inputValue.trim();
      setInputValue('');
      onSkillsChange([...skills, newSkill]);
      sessionStorage.removeItem('recommendedRoles');

      try {
        await axios.post(
          "http://localhost:8080/api/skills/add_skill",
          {
            userId: getUserIdFromToken(),
            skillName: newSkill,
          }
        );
      } catch (error) {
        console.error('Failed to add skill:', error);
      }
    }
  };

  const removeSkill = async (skillToRemove: string) => {
    try {
      const response = await axios.delete(
        "http://localhost:8080/api/skills/remove_skill",
        {
          data: {
            userId: getUserIdFromToken(),
            skillName: skillToRemove,
          },
        }
      );

      if (response.status === 200) {
        onSkillsChange(
          response.data.skills || skills.filter(skill => skill !== skillToRemove)
        );
        sessionStorage.removeItem('recommendedRoles');
      } else {
        console.error("Failed to remove skill");
      }
    } catch (error) {
      console.error('Failed to remove skill:', error);
    }
  };

  return (
    <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
      <h2 className="text-xl font-semibold text-gray-800 mb-4">Your Skills</h2>
      <div className="flex gap-2 mb-4">
        <input
          type="text"
          value={inputValue}
          onChange={(e: ChangeEvent<HTMLInputElement>) => setInputValue(e.target.value)}
          onKeyPress={(e: KeyboardEvent<HTMLInputElement>) => e.key === 'Enter' && addSkill()}
          placeholder="Add a skill (e.g., React, Python, SQL)"
          className="flex-1 px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
        <button
          onClick={addSkill}
          className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 flex items-center gap-2"
        >
          <Plus size={20} />
          Add
        </button>
      </div>
      <div className="flex flex-wrap gap-2">
        {skills.map((skill: string) => (
          <span
            key={skill}
            className="inline-flex items-center gap-2 px-3 py-1.5 bg-blue-50 text-blue-700 rounded-full"
          >
            {skill}
            <button
              onClick={() => removeSkill(skill)}
              className="hover:bg-blue-100 rounded-full p-0.5"
            >
              <X size={16} />
            </button>
          </span>
        ))}
      </div>
    </div>
  );
}
