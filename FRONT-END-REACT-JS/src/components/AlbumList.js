import React from 'react';
import { BsFillVinylFill} from 'react-icons/bs';

const List = (props) => {
  const { albums } = props;
  if (!albums || albums.length === 0) return <p>No albums in the store, sorry</p>;
  return (
    <div> 
      <h2 className='list-head'>Available Albums</h2>
      {albums.map((album) => {
        return (
             <div key={album.albumId}>
             {album.albumId !== 1 && <BsFillVinylFill />}
              <p> 
                <span className='album-text'><b>{album.title}</b> </span> 
                <span className='album-year'>({album.year})</span> 
              </p> 
              <p>
                <span className='album-description'>{album.info}</span>
              </p>
              <p><b>Links:</b> 
                <a href={album._links['List of all releases in the store for this album'].href} >Releases for this album</a> | 
                <a href={album._links['Info about the artist'].href}>Info about the artist</a>
              </p>
              <br />
            </div>
        );
      })}
    </div>
  );
};
export default List;
