import { gql } from '@apollo/client/core';

export const GET_MOVIES = gql`
  query GetMovies($filter: MovieFilter, $page: Int, $size: Int, $sort: MovieSort) {
    movies(filter: $filter, page: $page, size: $size, sort: $sort) {
      content {
        id title releaseYear genre rating runtime posterUrl
        directors { id name }
        cast { id characterName person { id name } }
        communityRating { voteAverage voteCount }
        reviewCount
      }
      totalElements totalPages currentPage size isFirst isLast hasNext hasPrevious
    }
  }
`;

export const GET_MOVIE = gql`
  query GetMovie($id: ID!) {
    movie(id: $id) {
      id title releaseYear genre rating runtime plot posterUrl tmdbId
      directors { id name birthYear country { name emoji } biography }
      cast { id characterName person { id name birthYear country { name emoji } biography photoUrl } }
      reviews { id score comment createdAt user { id username } }
    }
  }
`;

export const SEARCH_MOVIES = gql`
  query SearchMovies($title: String!) {
    searchMovies(title: $title) {
      id title releaseYear genre rating posterUrl
      directors { id name }
    }
  }
`;

export const GET_PEOPLE = gql`
  query GetPeople($page: Int, $size: Int) {
    people(page: $page, size: $size) {
      content { id name birthYear country { name emoji } photoUrl }
      totalElements totalPages currentPage size
    }
  }
`;

export const GET_PERSON = gql`
  query GetPerson($id: ID!) {
    person(id: $id) {
      id name birthYear country { name emoji } biography photoUrl
      directedMovies { id title releaseYear genre posterUrl rating }
      movieCastCredits { id characterName movie { id title releaseYear genre posterUrl rating } }
      createdShows { id title startYear endYear genre posterUrl rating }
      tvShowCastCredits { id characterName tvShow { id title startYear genre posterUrl rating } }
    }
  }
`;

export const SEARCH_PEOPLE = gql`
  query SearchPeople($name: String!) {
    searchPeople(name: $name) {
      id name birthYear country { name emoji } photoUrl
    }
  }
`;

export const GLOBAL_SEARCH = gql`
  query GlobalSearch($query: String!) {
    search(query: $query) {
      ... on Movie {
        __typename
        id title releaseYear genre rating posterUrl
      }
      ... on TvShow {
        __typename
        id title startYear endYear showGenre: genre rating posterUrl
      }
    }
    searchPeople(name: $query) {
      id name birthYear country { name emoji } photoUrl
    }
  }
`;

export const TMDB_SEARCH = gql`
  query TmdbSearch($title: String!) {
    tmdbSearch(title: $title) {
      tmdbId title releaseYear overview posterUrl rating
    }
  }
`;

export const GET_TV_SHOWS = gql`
  query GetTvShows($page: Int, $size: Int) {
    tvShows(page: $page, size: $size) {
      content {
        id title genre rating posterUrl startYear endYear seasons
        creators { id name }
        cast { id characterName person { id name } }
      }
      totalElements totalPages currentPage size
    }
  }
`;

export const GET_TV_SHOW = gql`
  query GetTvShow($id: ID!) {
    tvShow(id: $id) {
      id title genre rating posterUrl startYear endYear seasons plot
      creators { id name birthYear country { name emoji } biography photoUrl }
      cast { id characterName person { id name birthYear country { name emoji } photoUrl } }
      episodes { id seasonNumber episodeNumber title overview runtime airYear }
      reviews { id score comment createdAt user { id username } }
    }
  }
`;
