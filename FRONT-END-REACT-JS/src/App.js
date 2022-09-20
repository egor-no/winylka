import React, { useEffect, useState } from 'react';
import './App.css';
import List from './components/List';

function App() {
  const [appState, setAppState] = useState({
    albums: null,
  });

  useEffect(() => {
    const apiUrl = `http://localhost:8090/store/albums`;
    fetch(apiUrl)
      .then((res) => res.json())
      .then((data) => {
        setAppState({albums: data._embedded.albumList });
      });
  }, [setAppState]);
  
  return (
    <div className='App'>
      <div className='container'>
        <h1> 🎹 Winylka 🎵 </h1>
        <hr />
      </div>
      <div className='container'>
        <List albums={appState.albums} />
      </div>
      <footer>
        <div className='footer'>
          <hr />
          <span>That's it so far </span>
          <br /><br />
        </div>
      </footer>
    </div>
  );
}
export default App;
