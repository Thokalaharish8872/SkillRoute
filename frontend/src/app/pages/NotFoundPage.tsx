import { Link } from 'react-router';
import { Home, ArrowLeft } from 'lucide-react';

export function NotFoundPage() {
  return (
    <div className="p-8">
      <div className="max-w-2xl mx-auto text-center py-16">
        <div className="mb-8">
          <h1 className="text-9xl font-bold text-gray-200">404</h1>
          <h2 className="text-3xl font-bold text-gray-900 mt-4 mb-2">Page Not Found</h2>
          <p className="text-gray-600">
            The page you're looking for doesn't exist or has been moved.
          </p>
        </div>
        <div className="flex items-center justify-center gap-4">
          <Link
            to="/"
            className="flex items-center gap-2 px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
          >
            <Home size={20} />
            Go to Dashboard
          </Link>
          <button
            onClick={() => window.history.back()}
            className="flex items-center gap-2 px-6 py-3 border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors"
          >
            <ArrowLeft size={20} />
            Go Back
          </button>
        </div>
      </div>
    </div>
  );
}
