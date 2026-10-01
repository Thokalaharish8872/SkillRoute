import { Outlet } from "react-router";
import { Sidebar } from "../components/Sidebar";
import { Header } from "../components/Header";
import { useState, useEffect } from "react";
import axios from "axios";

// Key used to track when the session started (persisted across SPA navigations)
export const SESSION_START_KEY = "sr_session_start";

export function RootLayout() {
  const [streak, setStreak] = useState<number | string>('-');

  // Record session start time once per browser session
  useEffect(() => {
    if (!sessionStorage.getItem(SESSION_START_KEY)) {
      sessionStorage.setItem(SESSION_START_KEY, String(Date.now()));
    }
  }, []);

  useEffect(() => {
    const fetchStreak = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/streak/get_streak');
        setStreak(response.data.streak !== undefined ? response.data.streak : (response.data || 0));
      } catch (error) {
        console.error('Failed to fetch streak:', error);
      }
    };
    fetchStreak();
  }, []);

  return (
    <div className="flex h-screen bg-gray-50">
      <Sidebar />
      <div className="flex-1 flex flex-col overflow-hidden">
        <Header streak={streak} />
        <main className="flex-1 overflow-y-auto">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
