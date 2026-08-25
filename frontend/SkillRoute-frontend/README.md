# SkillRoute - Career Guidance Platform

A modern web dashboard for intelligent career guidance that helps users:
- Analyze and manage their skills
- Identify skill gaps
- Discover matching career paths
- Find learning resources (courses/tutorials)
- Track progress and learning streaks

## Tech Stack

- **React 18.3.1** - UI library
- **JavaScript/JSX** - Pure JavaScript (no TypeScript)
- **React Router 7** - Client-side routing
- **Tailwind CSS 4** - Utility-first CSS framework
- **Vite** - Build tool and dev server
- **Lucide React** - Icon library
- **Recharts** - Charts and data visualization

## Getting Started

### Prerequisites
- Node.js (v16 or higher)
- npm or pnpm

### Installation

1. **Extract the project folder** or clone it to your local machine

2. **Navigate to the project directory**
   ```bash
   cd skillroute
   ```

3. **Install dependencies**
   ```bash
   npm install
   ```
   
   Or if you use pnpm:
   ```bash
   pnpm install
   ```

4. **Start the development server**
   ```bash
   npm start
   ```
   
   Or:
   ```bash
   npm run dev
   ```

5. **Open your browser**
   - Navigate to `http://localhost:5173`
   - The app will automatically reload when you make changes

## Available Scripts

- `npm start` - Start development server
- `npm run dev` - Start development server (alias)
- `npm run build` - Build for production
- `npm run preview` - Preview production build locally

## Project Structure

```
skillroute/
├── src/
│   ├── app/
│   │   ├── components/      # Reusable UI components
│   │   │   ├── Header.tsx
│   │   │   ├── Sidebar.tsx
│   │   │   ├── SkillInput.tsx
│   │   │   ├── RecommendedRoles.tsx
│   │   │   ├── SkillGaps.tsx
│   │   │   ├── LearningRecommendations.tsx
│   │   │   └── ProgressTracker.tsx
│   │   ├── layouts/         # Layout components
│   │   │   └── RootLayout.tsx
│   │   ├── pages/           # Page components
│   │   │   ├── DashboardPage.tsx
│   │   │   ├── SkillsPage.tsx
│   │   │   ├── CareersPage.tsx
│   │   │   ├── RecommendationsPage.tsx
│   │   │   ├── ProgressPage.tsx
│   │   │   └── NotFoundPage.tsx
│   │   ├── App.tsx          # Main app component
│   │   └── routes.ts        # React Router configuration
│   ├── styles/
│   │   ├── index.css        # Global styles
│   │   └── theme.css        # Tailwind theme
│   └── main.tsx             # Application entry point
├── index.html               # HTML template
├── vite.config.ts           # Vite configuration
├── package.json             # Dependencies and scripts
└── README.md                # This file
```

## Features

### Dashboard
- Overview stats cards (Skills Mastered, Career Matches, Courses)
- Recent activity feed
- Quick action links

### My Skills
- Add/remove skills with tag interface
- Skill gap analysis with priority levels
- Skills organized by category (Frontend, Backend, Tools)

### Career Paths
- Recommended roles based on your skills
- Match percentage for each role
- Salary ranges and growth statistics
- Filter options

### Learning Recommendations
- Curated course suggestions
- Multiple platforms (YouTube, Coursera, Udemy)
- Difficulty levels (Beginner, Intermediate, Advanced)
- Filter by difficulty

### Progress Tracker
- Skill progress visualization
- Weekly activity chart
- Activity heatmap (GitHub-style)
- Learning statistics

## Customization

### Styling
- Tailwind CSS classes are used throughout
- Theme colors defined in `src/styles/theme.css`
- Modify color scheme by editing Tailwind configuration

### Mock Data
- All data is currently mock/sample data
- Replace with real API calls in components as needed

## Building for Production

```bash
npm run build
```

This creates an optimized production build in the `dist/` folder.

To preview the production build locally:

```bash
npm run preview
```

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## License

Private project - All rights reserved

## Support

For issues or questions, please refer to the project documentation or contact the development team.
