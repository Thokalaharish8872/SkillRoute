import { createBrowserRouter, Navigate } from "react-router";
import { RootLayout } from "./layouts/RootLayout";
import { DashboardPage } from "./pages/DashboardPage";
import { SkillsPage } from "./pages/SkillsPage";
import { CareersPage } from "./pages/CareersPage";
import { CareerPathPage } from "./pages/CareerPathPage";
import { ProgressPage } from "./pages/ProgressPage";
import { NotFoundPage } from "./pages/NotFoundPage";
import { LearningSheetPage } from "./pages/LearningSheetPage";
import { ProfilePage } from "./pages/ProfilePage";
import { AuthPage } from "./pages/AuthPage";
import { useAuth } from "./context/AuthContext";
import { LoadingSpinner } from "./components/LoadingSpinner";

function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const { isAuthenticated, isLoading } = useAuth();
  
  if (isLoading) {
    return <LoadingSpinner />;
  }
  
  return isAuthenticated ? <>{children}</> : <Navigate to="/auth" replace />;
}

function PublicRoute({ children }: { children: React.ReactNode }) {
  const { isAuthenticated, isLoading } = useAuth();
  
  if (isLoading) {
    return <LoadingSpinner />;
  }
  
  return isAuthenticated ? <Navigate to="/" replace /> : <>{children}</>;
}

export const router = createBrowserRouter([
  {
    path: "/auth",
    element: (
      <PublicRoute>
        <AuthPage />
      </PublicRoute>
    ),
  },
  {
    path: "/",
    Component: RootLayout,
    children: [
      {
        index: true,
        element: (
          <ProtectedRoute>
            <DashboardPage />
          </ProtectedRoute>
        ),
      },
      {
        path: "skills",
        element: (
          <ProtectedRoute>
            <SkillsPage />
          </ProtectedRoute>
        ),
      },
      {
        path: "careers",
        element: (
          <ProtectedRoute>
            <CareersPage />
          </ProtectedRoute>
        ),
      },
      {
        path: "career-path",
        element: (
          <ProtectedRoute>
            <CareerPathPage />
          </ProtectedRoute>
        ),
      },
      {
        path: "progress",
        element: (
          <ProtectedRoute>
            <ProgressPage />
          </ProtectedRoute>
        ),
      },
      {
        path: "learning-sheet",
        element: (
          <ProtectedRoute>
            <LearningSheetPage />
          </ProtectedRoute>
        ),
      },
      {
        path: "profile",
        element: (
          <ProtectedRoute>
            <ProfilePage />
          </ProtectedRoute>
        ),
      },
      { path: "*", Component: NotFoundPage },
    ],
  },
]);
