import { useParams, Link } from 'react-router-dom';
import { useQuery } from '@apollo/client/react';
import { GET_PERSON } from '../graphql/queries';
import MovieCard from '../components/MovieCard';

export default function PersonDetailPage() {
  const { id } = useParams();
  const { data, loading, error } = useQuery(GET_PERSON, { variables: { id } });

  if (loading) return (
    <div className="max-w-7xl mx-auto px-4 py-16 animate-pulse">
      <div className="flex gap-8">
        <div className="w-48 h-48 bg-zinc-800 rounded-xl shrink-0" />
        <div className="flex-1 space-y-4">
          <div className="h-8 bg-zinc-800 rounded w-1/3" />
          <div className="h-4 bg-zinc-800 rounded w-1/4" />
          <div className="h-20 bg-zinc-800 rounded" />
        </div>
      </div>
    </div>
  );

  if (error || !data?.person) return (
    <div className="max-w-7xl mx-auto px-4 py-16 text-center text-zinc-400">
      <p>Person not found.</p>
      <Link to="/people" className="text-yellow-400 hover:underline mt-4 inline-block">← Back to people</Link>
    </div>
  );

  const person = data.person;
  const directedMovies = person.directedMovies || [];
  const castCredits = person.movieCastCredits || [];
  const createdShows = person.createdShows || [];
  const tvCastCredits = person.tvShowCastCredits || [];

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <Link to="/people" className="text-zinc-400 hover:text-white text-sm flex items-center gap-1 mb-6 w-fit">
        ← Back to people
      </Link>

      <div className="flex flex-col sm:flex-row gap-8 mb-10">
        <div className="shrink-0">
          <div className="w-48 h-48 rounded-xl overflow-hidden bg-zinc-800 border border-zinc-700 flex items-center justify-center">
            {person.photoUrl ? (
              <img src={person.photoUrl} alt={person.name} className="w-full h-full object-cover"
                onError={e => { e.target.style.display = 'none'; }} />
            ) : (
              <svg className="w-20 h-20 text-zinc-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1}
                  d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
              </svg>
            )}
          </div>
        </div>

        <div className="flex-1">
          <h1 className="text-3xl md:text-4xl font-bold text-white mb-2">{person.name}</h1>
          <div className="flex flex-wrap gap-3 text-zinc-400 text-sm mb-4">
            {person.birthYear && <span>Born {person.birthYear}</span>}
            {person.country && <span>· {person.country.emoji} {person.country.name}</span>}
          </div>
          {person.biography && (
            <p className="text-zinc-300 leading-relaxed max-w-2xl">{person.biography}</p>
          )}
        </div>
      </div>

      {directedMovies.length > 0 && (
        <div className="mb-10">
          <h2 className="text-xl font-bold text-white mb-4">
            Directed <span className="text-zinc-500 font-normal text-base">({directedMovies.length} films)</span>
          </h2>
          <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-4">
            {directedMovies.slice().sort((a, b) => b.releaseYear - a.releaseYear).map(movie => (
              <MovieCard key={movie.id} movie={movie} />
            ))}
          </div>
        </div>
      )}

      {castCredits.length > 0 && (
        <div className="mb-10">
          <h2 className="text-xl font-bold text-white mb-4">
            Acting <span className="text-zinc-500 font-normal text-base">({castCredits.length} films)</span>
          </h2>
          <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-4">
            {castCredits.slice().sort((a, b) => b.movie.releaseYear - a.movie.releaseYear).map(credit => (
              <div key={credit.id} className="relative">
                <MovieCard movie={credit.movie} />
                {credit.characterName && (
                  <div className="mt-1 px-1 text-xs text-zinc-500 italic truncate">{credit.characterName}</div>
                )}
              </div>
            ))}
          </div>
        </div>
      )}

      {createdShows.length > 0 && (
        <div className="mb-10">
          <h2 className="text-xl font-bold text-white mb-4">
            Created Shows <span className="text-zinc-500 font-normal text-base">({createdShows.length})</span>
          </h2>
          <div className="flex flex-wrap gap-3">
            {createdShows.map(show => (
              <Link key={show.id} to={`/tvshow/${show.id}`}
                className="bg-zinc-900 border border-zinc-800 hover:border-zinc-600 rounded-lg px-4 py-2 transition-colors">
                <div className="text-white text-sm font-medium">{show.title}</div>
                <div className="text-zinc-500 text-xs">{show.startYear}{show.endYear ? `–${show.endYear}` : '–present'}</div>
              </Link>
            ))}
          </div>
        </div>
      )}

      {tvCastCredits.length > 0 && (
        <div className="mb-10">
          <h2 className="text-xl font-bold text-white mb-4">
            TV Roles <span className="text-zinc-500 font-normal text-base">({tvCastCredits.length})</span>
          </h2>
          <div className="flex flex-wrap gap-3">
            {tvCastCredits.map(credit => (
              <Link key={credit.id} to={`/tvshow/${credit.tvShow.id}`}
                className="bg-zinc-900 border border-zinc-800 hover:border-zinc-600 rounded-lg px-4 py-2 transition-colors">
                <div className="text-white text-sm font-medium">{credit.tvShow.title}</div>
                {credit.characterName && (
                  <div className="text-zinc-500 text-xs italic">{credit.characterName}</div>
                )}
              </Link>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}
