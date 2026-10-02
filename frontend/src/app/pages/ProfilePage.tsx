import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { API_BASE_URL } from '../utils/api';
import { useNavigate } from 'react-router';
import { useAuth } from '../context/AuthContext';
import { 
  User, 
  Mail, 
  MapPin, 
  Briefcase, 
  Github, 
  Settings, 
  Award, 
  Flame, 
  BookOpen,
  Edit2,
  Code2,
  Loader2,
  Save,
  TerminalSquare,
  LogOut
} from 'lucide-react';

// axios.defaults.withCredentials = true;

interface CodingProfiles {
  leetcode: string;
  github: string;
  codechef: string;
  codeforces: string;
}

interface Profile {
  id?: number;
  userId: number;
  userName: string;
  email: string;
  role: string;
  location: string;
  codingProfiles: CodingProfiles;
}

export function ProfilePage() {
  const navigate = useNavigate();
  const { logout } = useAuth();
  const [profile, setProfile] = useState<Profile | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState<Profile | null>(null);

  const handleLogout = () => {
    logout();
    navigate('/auth');
  };

  useEffect(() => {
    fetchProfile();
  }, []);

  const fetchProfile = async () => {
    try {
      setLoading(true);
      const response = await axios.get(`${API_BASE_URL}/api/profile/get_profile`);
      
      if (response.data) {
        const data = response.data;
        const profileData: Profile = {
          ...data,
          codingProfiles: data.codingProfiles || { leetcode: '', github: '', codechef: '', codeforces: '' }
        };
        
        setProfile(profileData);
        setFormData(profileData);
      }
    } catch (error) {
      console.error('Failed to fetch profile', error);
      setProfile(null);
      setFormData(null);
    } finally {
      setLoading(false);
    }
  };

  const handleSave = async () => {
    if (!formData) return;
    try {
      setSaving(true);
      const response = await axios.post(`${API_BASE_URL}/api/profile/update_profile`, formData);
      if (response.data) {
        const data = response.data;
        const profileData: Profile = {
          ...data,
          codingProfiles: data.codingProfiles || { leetcode: '', github: '', codechef: '', codeforces: '' }
        };
        setProfile(profileData);
        setFormData(profileData);
      } else {
        setProfile(formData);
      }
      setIsEditing(false);
    } catch (error:any) {
      console.error('Failed to update profile', error);
      alert('Failed to update profile on the server. Make sure the backend is running.');
      setProfile(formData);
      setIsEditing(false);

      console.log(error);

    if (error.response) {
        console.log("Status:", error.response.status);
        console.log("Data:", error.response.data);
    }

    alert("Request failed");
    } finally {
      setSaving(false);
    }
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>, field: keyof Profile) => {
    if (formData) {
      setFormData({ ...formData, [field]: e.target.value });
    }
  };

  const handleCodingProfileChange = (e: React.ChangeEvent<HTMLInputElement>, field: keyof CodingProfiles) => {
    if (formData) {
      setFormData({
        ...formData,
        codingProfiles: { ...formData.codingProfiles, [field]: e.target.value }
      });
    }
  };

  if (loading) {
    return (
      <div className="p-8 bg-gray-50 min-h-screen flex items-center justify-center">
        <div className="flex flex-col items-center gap-4">
          <Loader2 className="w-12 h-12 text-blue-600 animate-spin" />
          <p className="text-gray-500 font-medium">Loading your profile...</p>
        </div>
      </div>
    );
  }

  if (!profile || !formData) {
    return (
      <div className="p-8 bg-gray-50 min-h-screen flex items-center justify-center">
        <div className="flex flex-col items-center gap-4 text-center">
          <p className="text-gray-700 font-semibold text-xl">Profile not found</p>
          <p className="text-gray-500">Could not load your profile. Please make sure you are logged in and try again.</p>
          <button
            onClick={fetchProfile}
            className="px-4 py-2 bg-blue-600 text-white rounded-lg font-medium hover:bg-blue-700 transition-colors"
          >
            Retry
          </button>
        </div>
      </div>
    );
  }


  return (
    <div className="p-8 bg-gray-50 min-h-screen">
      <div className="max-w-5xl mx-auto space-y-8">
        
        {/* Header Title */}
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold text-gray-900 mb-2">My Profile</h1>
            <p className="text-gray-600">Manage your personal information and coding platforms.</p>
          </div>
          {isEditing ? (
            <div className="flex items-center gap-3">
              <button 
                onClick={() => {
                  setFormData(profile);
                  setIsEditing(false);
                }}
                className="px-4 py-2 text-gray-600 hover:bg-gray-100 rounded-lg font-medium transition-colors"
                disabled={saving}
              >
                Cancel
              </button>
              <button 
                onClick={handleSave}
                disabled={saving}
                className="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded-lg font-medium hover:bg-blue-700 transition-colors shadow-sm disabled:opacity-70"
              >
                {saving ? <Loader2 size={18} className="animate-spin" /> : <Save size={18} />}
                {saving ? 'Saving...' : 'Save Changes'}
              </button>
            </div>
          ) : (
            <div className="flex items-center gap-3">
              <button 
                onClick={() => setIsEditing(true)}
                className="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded-lg font-medium hover:bg-blue-700 transition-colors shadow-sm cursor-pointer"
              >
                <Edit2 size={18} />
                Edit Profile
              </button>
              <button 
                onClick={handleLogout}
                className="flex items-center gap-2 px-4 py-2 border border-red-200 text-red-600 hover:bg-red-50 rounded-lg font-medium transition-colors shadow-sm cursor-pointer"
              >
                <LogOut size={18} />
                Logout
              </button>
            </div>
          )}
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          
          {/* Left Column: Personal Info Card */}
          <div className="lg:col-span-1 space-y-8">
            <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
              <div className="h-24 bg-gradient-to-r from-blue-500 to-indigo-600"></div>
              <div className="px-6 pb-6 relative">
                <div className="w-24 h-24 bg-white rounded-full flex items-center justify-center border-4 border-white shadow-md absolute -top-27 text-blue-600 bg-blue-50">
                  <span className="text-3xl font-bold">{profile.userName ? profile.userName.substring(0, 2).toUpperCase() : 'U'}</span>
                </div>
                <div className="mt-14">
                  <h2 className="text-xl font-bold text-gray-900">{profile.userName}</h2>
                  <p className="text-sm font-medium text-gray-500 mb-4">{profile.role}</p>
                  
                  <div className="space-y-3">
                    <div className="flex items-center gap-3 text-gray-600 text-sm">
                      <Mail size={16} className="text-gray-400" />
                      {profile.email}
                    </div>
                    <div className="flex items-center gap-3 text-gray-600 text-sm">
                      <Briefcase size={16} className="text-gray-400" />
                      {profile.role}
                    </div>
                    <div className="flex items-center gap-3 text-gray-600 text-sm">
                      <MapPin size={16} className="text-gray-400" />
                      {profile.location}
                    </div>
                  </div>

                  <hr className="my-6 border-gray-100" />

                  <div className="space-y-3">
                    {profile.codingProfiles.github && (
                      <a href={`https://github.com/${profile.codingProfiles.github}`} target="_blank" rel="noreferrer" className="flex items-center gap-3 text-sm text-gray-600 hover:text-blue-600 transition-colors">
                        <Github size={18} />
                        {profile.codingProfiles.github}
                      </a>
                    )}
                    {profile.codingProfiles.leetcode && (
                      <a href={`https://leetcode.com/${profile.codingProfiles.leetcode}`} target="_blank" rel="noreferrer" className="flex items-center gap-3 text-sm text-gray-600 hover:text-orange-500 transition-colors">
                        <Code2 size={18} />
                        {profile.codingProfiles.leetcode} (LeetCode)
                      </a>
                    )}
                  </div>
                </div>
              </div>
            </div>

            {/* Quick Stats */}
            <div className="grid grid-cols-1 sm:grid-cols-3 lg:grid-cols-1 gap-4">
              <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-4 flex items-center gap-4">
                <div className="w-12 h-12 rounded-lg bg-orange-50 flex items-center justify-center">
                  <Flame className="text-orange-500" size={24} />
                </div>
                <div>
                  <div className="text-2xl font-bold text-gray-900">14</div>
                  <div className="text-xs font-medium text-gray-500 uppercase tracking-wide">Day Streak</div>
                </div>
              </div>

              <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-4 flex items-center gap-4">
                <div className="w-12 h-12 rounded-lg bg-emerald-50 flex items-center justify-center">
                  <Award className="text-emerald-500" size={24} />
                </div>
                <div>
                  <div className="text-2xl font-bold text-gray-900">2</div>
                  <div className="text-xs font-medium text-gray-500 uppercase tracking-wide">Certifications</div>
                </div>
              </div>
            </div>
          </div>

          {/* Right Column: Settings */}
          <div className="lg:col-span-2 space-y-8">
            
            {/* Account Settings */}
            <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6 md:p-8">
              <h3 className="text-lg font-bold text-gray-900 mb-6 flex items-center gap-2">
                <User size={20} className="text-blue-600" />
                Personal Information
              </h3>
              
              <div className="space-y-6">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">Full Name</label>
                    <input 
                      type="text" 
                      disabled={!isEditing}
                      value={formData.userName}
                      onChange={(e) => handleInputChange(e, 'userName')}
                      className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none disabled:bg-gray-50 disabled:text-gray-500 transition-colors"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">Email Address</label>
                    <input 
                      type="email" 
                      disabled={!isEditing}
                      value={formData.email}
                      onChange={(e) => handleInputChange(e, 'email')}
                      className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none disabled:bg-gray-50 disabled:text-gray-500 transition-colors"
                    />
                  </div>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">Role</label>
                    <input 
                      type="text" 
                      disabled={!isEditing}
                      value={formData.role}
                      onChange={(e) => handleInputChange(e, 'role')}
                      className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none disabled:bg-gray-50 disabled:text-gray-500 transition-colors"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">Location</label>
                    <input 
                      type="text" 
                      disabled={!isEditing}
                      value={formData.location}
                      onChange={(e) => handleInputChange(e, 'location')}
                      className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none disabled:bg-gray-50 disabled:text-gray-500 transition-colors"
                    />
                  </div>
                </div>
              </div>
            </div>

            {/* Coding Profiles Settings */}
            <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6 md:p-8">
              <h3 className="text-lg font-bold text-gray-900 mb-6 flex items-center gap-2">
                <TerminalSquare size={20} className="text-blue-600" />
                Coding Platforms Handles
              </h3>
              
              <div className="space-y-6">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">LeetCode Username</label>
                    <input 
                      type="text" 
                      disabled={!isEditing}
                      placeholder="e.g. johndoe"
                      value={formData.codingProfiles.leetcode}
                      onChange={(e) => handleCodingProfileChange(e, 'leetcode')}
                      className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none disabled:bg-gray-50 disabled:text-gray-500 transition-colors"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">GitHub Username</label>
                    <input 
                      type="text" 
                      disabled={!isEditing}
                      placeholder="e.g. johndoe"
                      value={formData.codingProfiles.github}
                      onChange={(e) => handleCodingProfileChange(e, 'github')}
                      className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none disabled:bg-gray-50 disabled:text-gray-500 transition-colors"
                    />
                  </div>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">CodeChef Username</label>
                    <input 
                      type="text" 
                      disabled={!isEditing}
                      placeholder="e.g. johndoe"
                      value={formData.codingProfiles.codechef}
                      onChange={(e) => handleCodingProfileChange(e, 'codechef')}
                      className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none disabled:bg-gray-50 disabled:text-gray-500 transition-colors"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">CodeForces Username</label>
                    <input 
                      type="text" 
                      disabled={!isEditing}
                      placeholder="e.g. johndoe"
                      value={formData.codingProfiles.codeforces}
                      onChange={(e) => handleCodingProfileChange(e, 'codeforces')}
                      className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none disabled:bg-gray-50 disabled:text-gray-500 transition-colors"
                    />
                  </div>
                </div>
              </div>
            </div>

          </div>
        </div>

      </div>
    </div>
  );
}
