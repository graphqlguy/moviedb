# Frontend Port + Seed Data Design

**Date:** 2026-07-11
**Status:** Approved (pending final spec review)

## Goal

Port the React frontend and the rich seed dataset from
`/Users/zkozina/MyProjects/GraphQL/courses/graphQLMovieDB` into this project,
adapted to this project's existing GraphQL schema. **No backend schema, entity,
or resolver changes.** Frontend features this backend does not support stay
visible but disabled, and are tracked in the Deferred Features list below.

## Source material

- Frontend: `graphQLMovieDB/frontend/` — Vite + React 19 + Apollo Client 4 +
  Tailwind 4 + react-router 7. Pages: Home (movie grid with filter/sort/
  pagination), MovieDetail, TvShows, TvShowDetail, Persons, PersonDetail,
  Login, Register, Admin. Components: Navbar, MovieCard, Pagination,
  ReviewSection. Auth via JWT in localStorage (`AuthContext`).
- Seed data: `graphQLMovieDB/.../config/DataInitializer.java` — ~46 movies
  (plot, runtime, posterUrl, tmdbId, directors, cast), 3 TV shows (Friends,
  Seinfeld, Game of Thrones) with 57 episodes and casts, ~100 persons,
  admin/user accounts.

## Design

### 1. Frontend copy and wiring

- Copy `frontend/` sources into this repo root at `frontend/` (exclude
  `node_modules`, `.vite`, lockfile artifacts of the old install).
- `vite.config.js`: change dev-server proxy target `http://localhost:8081` →
  `http://localhost:8080`; remove `ws: true` (no subscriptions here).
- No backend config changes: `application.yaml` already allows the Vite
  origin (`http://localhost:5173`) via CORS.

### 2. GraphQL operations layer (`frontend/src/graphql/`)

This is the only schema-coupled layer; all edits concentrate here.

`queries.js`:
- `GET_MOVIES`: remove `inTheaters`, `reviewCount`. Keep `communityRating`.
- `GET_MOVIE`: remove `inTheaters`, `biography`, `photoUrl`.
- `GET_PERSONS`, `SEARCH_PERSONS`, `GLOBAL_SEARCH`: remove `photoUrl`.
- `GET_PERSON`: remove `biography`, `photoUrl`, and the whole
  `createdShows` block.
- `GET_TV_SHOW`: remove `tmdbId`, `reviews` block, and `biography`/`photoUrl`
  inside `creators`/`cast` person selections.
- `GET_ME`: delete (unused, and no `me` query in this schema).

`mutations.js`:
- `ADD_MOVIE_REVIEW` → this schema's
  `createMovieReview(input: { movieId, score, comment })`.
- `DELETE_REVIEW`: selection `{ success message }` → `{ success deletedId }`
  (returns `DeleteReviewResponse` here).
- Remove `REGISTER`, `CREATE_MOVIE`, `UPDATE_MOVIE`, `ADD_TV_SHOW_REVIEW`
  (no such mutations in this schema).
- Keep unchanged: `LOGIN` (AuthResponse has same shape), `DELETE_MOVIE`
  (`DeleteMovieResponse` includes `success`/`message`), `CREATE_PERSON`.

`subscriptions.js`: delete file.

`apollo/client.js`: remove `GraphQLWsLink`/`split`; keep HTTP link +
auth-header link. Drop `graphql-ws` from `package.json`.

### 3. Pages/components — disabled-feature treatment

Chosen approach: **keep UI visible, disable what the backend can't do**, with
a short notice on each.

- **RegisterPage**: form stays; submit disabled; banner "Registration isn't
  supported by this backend" with a pointer to demo credentials
  (admin/admin123, user/user123) and a link to Login.
- **AdminPage**: TMDB search + autofill keeps working (`tmdbSearch` exists);
  Save/Update button disabled with a banner that `createMovie`/`updateMovie`
  aren't in this schema.
- **ReviewSection**: fully functional for movies (create via
  `createMovieReview`, delete via `deleteReview`). On TV show pages it renders
  only a notice that TV show reviews aren't supported. All
  subscription/live-toast code removed.
- **MovieDetailPage**: admin Delete button keeps working. Renders without
  `inTheaters` badge.
- **MovieCard**: drop review-count and in-theaters badges.
- **PersonsPage / PersonDetailPage / person avatars anywhere**: initial-letter
  avatar fallback (no `photoUrl`); no biography section; no "Created shows"
  section on person detail (acting credits for TV shows remain via
  `tvShowCastCredits`).

### 4. Seed data

Replace the body of this project's `DataInitializer` (keep its
`@Component implements CommandLineRunner` style; may adopt the source's
`CastEntry` record + helper methods for readability):

- Port all ~46 movies with `title`, `releaseYear`, `genre`, `rating`,
  `runtime`, `plot`, `posterUrl`, `tmdbId`, directors, cast. Drop the
  source's `inTheaters` argument (no such column here).
- Port all persons with `name`, `birthYear`, `nationality` only (drop
  `biography`, `photoUrl` — no such columns here).
- Port 3 TV shows with creators, casts, and all 57 episodes. Keep `tmdbId`
  (the TvShow entity has the column even though the GraphQL type doesn't
  expose it).
- Users unchanged (admin/admin123 ADMIN, user/user123 USER — identical in
  both projects).
- Do not port watchlist seeding (no watchlist feature here).

### 5. Error handling

- Frontend keeps the source's existing error surfaces (mutation errors shown
  inline; TMDB-key-missing notice on Admin page already handled).
- No new backend error paths are introduced.

### 6. Verification

1. `./mvnw compile` (or `spring-boot:run`) — backend starts, seeding logs
   ~46 movies / 3 shows.
2. `cd frontend && npm install && npm run dev`.
3. Browser walkthrough: home grid shows posters + pagination/filters; TV
   shows page lists 3 shows, detail shows episodes; person pages render with
   initial avatars; login as `user`, post + delete a review on a movie;
   login as `admin`, delete a movie; Register and Admin-save show their
   disabled notices; global search returns movies, shows, and people.

## Deferred features (decision list)

Kept as visible-but-disabled UI. Each can be picked up later as its own task;
backend cost noted.

| # | Feature | Backend work needed |
|---|---------|---------------------|
| 1 | User registration | `register` mutation + `RegisterInput`/AuthPayload wiring |
| 2 | Admin: create movie | `createMovie(input)` mutation + `CreateMovieInput` |
| 3 | Admin: edit movie | `updateMovie(id, input)` mutation + `UpdateMovieInput` |
| 4 | TV show reviews | `TvShow.reviews` field + `addTvShowReview`; `Review` entity is movie-only today |
| 5 | Live review notifications | GraphQL `Subscription` over WebSocket (`reviewAdded`) — largest item |
| 6 | Person photos | `photoUrl` column + schema field on `Person` |
| 7 | Person biographies | `biography` column + schema field on `Person` |
| 8 | "Created shows" on person detail | Only a `Person.createdShows` reverse resolver — data already exists via `TvShow.creators`; cheapest item |

Cosmetic, not tracked: in-theaters badge (`Movie.inTheaters` column absent —
note `MovieFilter.inTheaters` input exists but has nothing to filter on) and
review-count badge (`Movie.reviewCount`).

## Out of scope

- Any backend schema/entity/resolver change.
- README/tutorial updates (only required before pushing a class branch).
- Production build/deploy setup for the frontend.
