# SkillRoute - Quick Setup Instructions

## Step 1: Download and Extract
You have the `skillroute-project` folder ready to use.

## Step 2: Open in VS Code
1. Open VS Code
2. File → Open Folder
3. Select the `skillroute-project` folder

## Step 3: Install Dependencies
Open the integrated terminal in VS Code (Ctrl+` or View → Terminal) and run:

```bash
npm install
```

**Note:** If you prefer pnpm, run `pnpm install` instead.

## Step 4: Start the Development Server
```bash
npm start
```

The app will open at `http://localhost:5173`

## That's it! 🎉

Your SkillRoute dashboard is now running locally.

### Available Commands:
- `npm start` - Start dev server
- `npm run dev` - Start dev server (same as above)
- `npm run build` - Build for production
- `npm run preview` - Preview production build

### Troubleshooting:

**Port already in use?**
- Change the port in `vite.config.ts` or kill the process using port 5173

**Missing dependencies?**
- Delete `node_modules` and `package-lock.json`, then run `npm install` again

**React/ReactDOM errors?**
- Make sure you ran `npm install` successfully
- Check that `package.json` has react and react-dom listed

### Project Structure:
```
skillroute-project/
├── src/
│   ├── app/              # All app components
│   │   ├── components/   # Reusable components
│   │   ├── pages/        # Page components
│   │   ├── layouts/      # Layout components
│   │   ├── App.tsx       # Main app
│   │   └── routes.ts     # Router config
│   ├── styles/           # CSS files
│   └── main.tsx          # Entry point
├── index.html            # HTML template
├── package.json          # Dependencies
└── vite.config.ts        # Vite config
```

### Tech Stack:
- React 18.3.1
- React Router 7
- Tailwind CSS 4
- Vite
- JavaScript (no TypeScript syntax, just .tsx extensions)

Enjoy building with SkillRoute! 🚀
