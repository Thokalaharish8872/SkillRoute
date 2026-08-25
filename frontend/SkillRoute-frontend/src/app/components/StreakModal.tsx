import { X, Flame, Sparkles } from 'lucide-react';
import { useEffect, useState } from 'react';
import confetti from 'canvas-confetti';

interface StreakModalProps {
  isOpen: boolean;
  onClose: () => void;
  streakDays: number;
}

export function StreakModal({ isOpen, onClose, streakDays }: StreakModalProps) {
  const [isVisible, setIsVisible] = useState(false);

  useEffect(() => {
    if (isOpen) {
      setIsVisible(true);
      // Trigger rich confetti when modal opens
      const duration = 3 * 1000;
      const animationEnd = Date.now() + duration;
      const defaults = { startVelocity: 30, spread: 360, ticks: 60, zIndex: 100000, colors: ['#ff8a00', '#e52e71', '#ffcd00', '#ffffff'] };

      const randomInRange = (min: number, max: number) => Math.random() * (max - min) + min;

      const interval: any = setInterval(function() {
        const timeLeft = animationEnd - Date.now();

        if (timeLeft <= 0) {
          return clearInterval(interval);
        }

        const particleCount = 50 * (timeLeft / duration);
        confetti(Object.assign({}, defaults, { particleCount, origin: { x: randomInRange(0.1, 0.3), y: Math.random() - 0.2 } }));
        confetti(Object.assign({}, defaults, { particleCount, origin: { x: randomInRange(0.7, 0.9), y: Math.random() - 0.2 } }));
      }, 250);
    } else {
      setTimeout(() => setIsVisible(false), 400); // Wait for fade out animation
    }
  }, [isOpen]);

  if (!isOpen && !isVisible) return null;

  return (
    <div className={`fixed inset-0 z-50 flex items-center justify-center p-4 transition-all duration-500 ${isOpen ? 'opacity-100' : 'opacity-0'}`}>
      {/* Deep Blur Backdrop */}
      <div 
        className="absolute inset-0 bg-slate-900/40 backdrop-blur-md transition-opacity duration-500"
        onClick={onClose}
      />
      
      {/* Modal Content - Glassmorphic */}
      <div className={`relative w-full max-w-sm bg-white/95 backdrop-blur-xl rounded-[2.5rem] shadow-[0_20px_60px_-15px_rgba(0,0,0,0.3)] border border-white/60 overflow-hidden transform transition-all duration-500 ease-out ${isOpen ? 'scale-100 translate-y-0 opacity-100' : 'scale-90 translate-y-8 opacity-0'}`}>
        
        {/* Abstract Animated Background Blobs */}
        <div className="absolute -top-32 -left-24 w-64 h-64 bg-orange-400 rounded-full mix-blend-multiply filter blur-3xl opacity-20 animate-pulse"></div>
        <div className="absolute -bottom-32 -right-24 w-64 h-64 bg-amber-300 rounded-full mix-blend-multiply filter blur-3xl opacity-20 animate-pulse" style={{ animationDelay: '1s' }}></div>
        
        {/* Floating Close Button */}
        <button 
          onClick={onClose} 
          className="absolute top-5 right-5 z-20 p-2.5 bg-slate-100/50 hover:bg-slate-100 rounded-full text-slate-400 hover:text-slate-700 backdrop-blur-md transition-all duration-300 hover:rotate-90"
        >
          <X className="w-5 h-5" />
        </button>

        <div className="px-8 pt-14 pb-10 flex flex-col items-center text-center relative z-10">
          
          {/* Flame Icon Container with Rich Glows */}
          <div className="relative mb-10 group cursor-default">
            {/* Outer Glow */}
            <div className="absolute inset-0 bg-orange-500 blur-2xl opacity-40 rounded-full group-hover:scale-125 transition-transform duration-700 ease-out"></div>
            {/* Pulsing Core Glow */}
            <div className="absolute inset-0 bg-gradient-to-tr from-orange-600 to-yellow-400 blur-xl opacity-60 rounded-full animate-pulse"></div>
            
            {/* Main Icon Circle */}
            <div className="relative w-36 h-36 bg-gradient-to-b from-orange-400 to-orange-600 rounded-full flex items-center justify-center shadow-2xl shadow-orange-500/40 border-[8px] border-white transform group-hover:-translate-y-2 group-hover:scale-105 transition-all duration-500 ease-out">
              <Flame className="w-16 h-16 text-white drop-shadow-lg transform group-hover:scale-110 transition-transform duration-500" fill="currentColor" />
              {/* Decorative Sparkles */}
              <Sparkles className="absolute top-3 right-4 w-6 h-6 text-yellow-200 animate-pulse" />
              <Sparkles className="absolute bottom-4 left-4 w-4 h-4 text-orange-200 animate-pulse" style={{ animationDelay: '0.5s' }} />
            </div>
          </div>

          {/* Typography */}
          <h3 className="text-xs font-black tracking-[0.2em] text-orange-500/80 uppercase mb-3">
            Daily Goal Achieved
          </h3>
          
          <h2 className="text-4xl font-extrabold text-slate-900 mb-3 tracking-tight">
            <span className="text-transparent bg-clip-text bg-gradient-to-r from-orange-500 to-amber-400">
              {streakDays} Day
            </span> Streak
          </h2>
          
          <p className="text-slate-500 font-medium mb-10 leading-relaxed max-w-[260px]">
            You're on fire! Consistency is the true key to mastery. See you tomorrow.
          </p>
          
          {/* Premium Gradient Button */}
          <button 
            onClick={onClose}
            className="relative w-full group rounded-2xl p-[2px] overflow-hidden shadow-lg shadow-orange-500/20 hover:shadow-orange-500/40 transition-all duration-300 transform hover:-translate-y-0.5 active:translate-y-0"
          >
            <span className="absolute inset-0 bg-gradient-to-r from-orange-500 via-amber-400 to-orange-500 rounded-2xl opacity-80 group-hover:opacity-100 transition-opacity duration-300 bg-[length:200%_auto] animate-gradient"></span>
            <div className="relative w-full px-8 py-4 bg-white rounded-[14px] flex items-center justify-center gap-2 group-hover:bg-opacity-0 transition-all duration-300">
              <span className="font-bold text-slate-800 group-hover:text-white transition-colors duration-300">Continue Learning</span>
            </div>
          </button>
          
        </div>
      </div>
    </div>
  );
}
