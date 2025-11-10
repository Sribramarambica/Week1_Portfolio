<!--
Complete Weather Application (single-file)
How to use:
 1. Get a free API key from OpenWeatherMap: https://openweathermap.org/api (Current Weather Data)
 2. Replace the value of `const API_KEY = 'YOUR_API_KEY_HERE'` below with your key.
 3. Open this file in a browser (or deploy to GitHub Pages).

Features included:
 - Search by city name
 - Use browser geolocation to get current location weather
 - Async/await fetch with error handling and user-friendly messages
 - Loading spinner and graceful fallbacks
 - Saves last searched city to localStorage
 - Responsive, accessible UI
 - Easy to style/extend (add forecast, background images, etc.)

Deployment:
 - Create a new GitHub repo, add this file as index.html, push, then enable GitHub Pages in repo settings (branch: main, root).
 - Or use any static host (Netlify, Vercel, Surge).
-->

<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width,initial-scale=1" />
  <title>Weather — Week 4 Project</title>
  <style>
    :root{
      --bg:#0f172a; --card:#0b1220; --accent:#22c1c3; --muted:#94a3b8; --glass: rgba(255,255,255,0.03);
      --radius:14px; --maxw:760px;
      color-scheme: dark;
    }
    *{box-sizing:border-box}
    html,body{height:100%;margin:0;font-family:Inter, ui-sans-serif, system-ui, -apple-system, 'Segoe UI', Roboto, 'Helvetica Neue', Arial;color:#e6eef7;background:linear-gradient(180deg,#021021 0%, #071229 60%);}
    .wrap{max-width:var(--maxw);margin:36px auto;padding:28px;background:linear-gradient(180deg, rgba(255,255,255,0.02), rgba(255,255,255,0.01));border-radius:20px;box-shadow:0 6px 30px rgba(2,6,23,0.7);}
    header{display:flex;align-items:center;gap:16px}
    h1{margin:0;font-size:20px}
    p.lead{margin:0;color:var(--muted);font-size:13px}
    .search{margin-top:18px;display:flex;gap:10px}
    input[type="search"]{flex:1;padding:12px 14px;border-radius:12px;border:1px solid rgba(255,255,255,0.04);background:var(--glass);color:inherit;font-size:15px}
    button{padding:10px 14px;border-radius:12px;border:none;background:linear-gradient(90deg,var(--accent),#5eead4);color:#042a2a;font-weight:600;cursor:pointer}
    button.secondary{background:transparent;border:1px solid rgba(255,255,255,0.06);color:var(--muted)}
    .grid{display:grid;grid-template-columns:1fr 1fr;gap:18px;margin-top:18px}
    .card{background:linear-gradient(180deg, rgba(255,255,255,0.02), rgba(0,0,0,0.02));padding:18px;border-radius:var(--radius);min-height:140px}
    .main-weather{display:flex;align-items:center;gap:16px}
    .main-weather img{width:96px;height:96px}
    .temp{font-size:42px;font-weight:700}
    .desc{font-size:15px;color:var(--muted)}
    .meta{display:flex;gap:12px;margin-top:10px;color:var(--muted);font-size:13px}
    .small{font-size:13px;color:var(--muted)}
    footer{margin-top:16px;color:var(--muted);font-size:13px}
    .message{padding:10px;border-radius:10px;background:rgba(255,255,255,0.02);border:1px solid rgba(255,255,255,0.02);color:var(--muted)}
    .hidden{display:none}
    @media (max-width:720px){.grid{grid-template-columns:1fr}.main-weather img{width:72px;height:72px}.temp{font-size:34px}}
    .spinner{width:22px;height:22px;border-radius:50%;border:3px solid rgba(255,255,255,0.08);border-top-color:var(--accent);animation:spin 1s linear infinite}
    @keyframes spin{to{transform:rotate(360deg)}}
  </style>
</head>
<body>
  <div class="wrap" role="main">
    <header>
      <div>
        <h1>Weather App — Week 4 Final Project</h1>
        <p class="lead">Search any city or use your current location. Data from OpenWeatherMap.</p>
      </div>
    </header>

    <div class="search" aria-label="Search weather">
      <input id="cityInput" type="search" placeholder="Enter city name (e.g. London)" aria-label="City name" />
      <button id="searchBtn">Search</button>
      <button id="geoBtn" class="secondary" title="Use my location">Use my location</button>
    </div>

    <div id="statusMsg" class="message hidden" role="status" aria-live="polite"></div>

    <div class="grid" style="margin-top:14px">
      <section class="card" id="weatherCard" aria-label="Current weather" hidden>
        <div class="main-weather">
          <img id="weatherIcon" src="" alt="weather icon" />
          <div>
            <div style="display:flex;align-items:center;gap:8px">
              <div class="temp" id="temp">--°C</div>
              <div class="desc" id="description">—</div>
            </div>
            <div class="meta">
              <div id="cityName">—</div>
              <div id="country">—</div>
            </div>
            <div class="meta" style="margin-top:8px">
              <div>Humidity: <span id="humidity">—</span>%</div>
              <div>Wind: <span id="wind">—</span> m/s</div>
            </div>
          </div>
        </div>
      </section>

      <aside class="card" id="detailsCard" hidden>
        <h3 style="margin-top:0;margin-bottom:10px">Details</h3>
        <div class="small">Feels like: <span id="feels">—</span>°C</div>
        <div class="small">Pressure: <span id="pressure">—</span> hPa</div>
        <div class="small" style="margin-top:8px">Coordinates: <span id="coords">—</span></div>
        <div style="margin-top:12px;color:var(--muted);font-size:13px">Tip: try cities like "New York", "Mumbai", "Sydney" or use the location button.</div>
      </aside>
    </div>

    <footer>
      <div class="small">Built for Week 4: Frontend Integration — demo app. Last searched city saved in browser.</div>
    </footer>
  </div>

  <script>
    // ===== CONFIG =====
    const API_KEY = 'YOUR_API_KEY_HERE'; // <-- replace with your OpenWeatherMap API key
    const BASE_URL = 'https://api.openweathermap.org/data/2.5/weather';

    // ===== UI =====
    const cityInput = document.getElementById('cityInput');
    const searchBtn = document.getElementById('searchBtn');
    const geoBtn = document.getElementById('geoBtn');
    const statusMsg = document.getElementById('statusMsg');
    const weatherCard = document.getElementById('weatherCard');
    const detailsCard = document.getElementById('detailsCard');

    const weatherIcon = document.getElementById('weatherIcon');
    const tempEl = document.getElementById('temp');
    const descriptionEl = document.getElementById('description');
    const cityNameEl = document.getElementById('cityName');
    const countryEl = document.getElementById('country');
    const humidityEl = document.getElementById('humidity');
    const windEl = document.getElementById('wind');
    const feelsEl = document.getElementById('feels');
    const pressureEl = document.getElementById('pressure');
    const coordsEl = document.getElementById('coords');

    function showMessage(text, isError=false){
      statusMsg.textContent = text;
      statusMsg.classList.remove('hidden');
      statusMsg.style.color = isError ? '#ffb4b4' : '';
    }
    function hideMessage(){ statusMsg.classList.add('hidden'); }

    function setLoading(loading=true){
      if(loading){
        statusMsg.innerHTML = '<div style="display:inline-flex;gap:8px;align-items:center"><span class="spinner" aria-hidden="true"></span>Loading...</div>';
        statusMsg.classList.remove('hidden');
      } else {
        hideMessage();
      }
    }

    function saveLastCity(city){
      try{ localStorage.setItem('lastCity', city); }catch(e){/* ignore */}
    }
    function loadLastCity(){
      try{ return localStorage.getItem('lastCity'); }catch(e){return null}
    }

    async function fetchWeatherByCity(city){
      if(!API_KEY || API_KEY === 'YOUR_API_KEY_HERE'){
        showMessage('Please set your OpenWeatherMap API key in the source (API_KEY).', true);
        return;
      }

      setLoading(true);
      try{
        const url = `${BASE_URL}?q=${encodeURIComponent(city)}&units=metric&appid=${API_KEY}`;
        const res = await fetch(url);
        if(!res.ok){
          if(res.status === 404) throw new Error('City not found. Try another name.');
          if(res.status === 401) throw new Error('Invalid API key. Check your API_KEY.');
          throw new Error(`Server error: ${res.status}`);
        }
        const data = await res.json();
        renderWeather(data);
        saveLastCity(city);
      }catch(err){
        showMessage(err.message || 'Failed to fetch weather', true);
      }finally{ setLoading(false); }
    }

    async function fetchWeatherByCoords(lat, lon){
      if(!API_KEY || API_KEY === 'YOUR_API_KEY_HERE'){
        showMessage('Please set your OpenWeatherMap API key in the source (API_KEY).', true);
        return;
      }
      setLoading(true);
      try{
        const url = `${BASE_URL}?lat=${lat}&lon=${lon}&units=metric&appid=${API_KEY}`;
        const res = await fetch(url);
        if(!res.ok) throw new Error(`Server error: ${res.status}`);
        const data = await res.json();
        renderWeather(data);
        if(data && data.name) saveLastCity(data.name);
      }catch(err){
        showMessage(err.message || 'Failed to fetch weather', true);
      }finally{ setLoading(false); }
    }

    function renderWeather(data){
      if(!data) return;
      weatherCard.hidden = false;
      detailsCard.hidden = false;
      const c = data;
      const icon = c.weather && c.weather[0] ? c.weather[0].icon : '01d';
      weatherIcon.src = `https://openweathermap.org/img/wn/${icon}@2x.png`;
      weatherIcon.alt = c.weather && c.weather[0] ? c.weather[0].description : 'weather';
      tempEl.textContent = Math.round(c.main.temp) + '°C';
      descriptionEl.textContent = c.weather && c.weather[0] ? capitalize(c.weather[0].description) : '';
      cityNameEl.textContent = c.name || '—';
      countryEl.textContent = c.sys && c.sys.country ? `, ${c.sys.country}` : '';
      humidityEl.textContent = c.main.humidity ?? '—';
      windEl.textContent = c.wind && c.wind.speed ? c.wind.speed : '—';
      feelsEl.textContent = c.main.feels_like ? Math.round(c.main.feels_like) : '—';
      pressureEl.textContent = c.main.pressure ?? '—';
      coordsEl.textContent = (c.coord && c.coord.lat && c.coord.lon) ? `${c.coord.lat.toFixed(2)}, ${c.coord.lon.toFixed(2)}` : '—';
    }

    function capitalize(s){ if(!s) return s; return s.charAt(0).toUpperCase() + s.slice(1); }

    // ===== Events =====
    searchBtn.addEventListener('click', ()=>{
      const v = cityInput.value.trim();
      if(!v){ showMessage('Please enter a city name.', true); return; }
      fetchWeatherByCity(v);
    });

    cityInput.addEventListener('keydown', (e)=>{ if(e.key === 'Enter'){ searchBtn.click(); } });

    geoBtn.addEventListener('click', ()=>{
      if(!navigator.geolocation){ showMessage('Geolocation not supported by your browser.', true); return; }
      setLoading(true);
      navigator.geolocation.getCurrentPosition(pos=>{
        fetchWeatherByCoords(pos.coords.latitude, pos.coords.longitude);
      }, err=>{
        setLoading(false);
        showMessage('Unable to access location: ' + err.message, true);
      }, {timeout:10000});
    });

    // Load last city on start
    window.addEventListener('load', ()=>{
      const last = loadLastCity();
      if(last){ cityInput.value = last; fetchWeatherByCity(last); }
    });

    // Friendly global error handler
    window.addEventListener('unhandledrejection', (e)=>{ console.warn('Unhandled rejection', e); showMessage('Network or unexpected error occurred.', true); });
  </script>
</body>
</html>
