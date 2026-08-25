import { Outlet } from "react-router";
import { Sidebar } from "../components/Sidebar";
import { Header } from "../components/Header";
import { useState, useEffect } from "react";
import axios from "axios";

export function RootLayout() {
  const [streak, setStreak] = useState<number | string>('-');

  useEffect(() => {
    const fetchStreak = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/streak/get_streak?userId=1');
        setStreak(response.data.streak !== undefined ? response.data.streak : (response.data || 0));
      } catch (error) {
        console.error('Failed to fetch streak:', error);
      }
    };
    fetchStreak();
  }, []);

  // Time tracking effect
  useEffect(() => {
    const sessionStartTime = Date.now();

    const handleUnload = () => {

      console.log("Unload fired");
      const sessionEndTime = Date.now();
      const durationSeconds = Math.floor((sessionEndTime - sessionStartTime) / 1000);

      // Only send if they spent some actual time
      if (durationSeconds > 0) {
        const payload = JSON.stringify({
          userId: 1,
          durationSeconds: durationSeconds
        });
        
        // Use a Blob to send JSON via sendBeacon
        const blob = new Blob([payload], { type: 'application/json' });
        
        // Replace with your exact tracking endpoint URL
fetch("http://localhost:8080/api/progress/update_active_time", {
    method: "POST",
    headers: {
        "Content-Type": "application/json",
    },
    body: JSON.stringify({
        userId: 1,
        durationSeconds,
    }),
    keepalive: true,
});
        console.log("success");
      }
    };

    // 'beforeunload' fires right before the tab/window is closed or refreshed
    window.addEventListener('beforeunload', handleUnload);

    return () => {
      window.removeEventListener('beforeunload', handleUnload);
    };
  }, [])

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
