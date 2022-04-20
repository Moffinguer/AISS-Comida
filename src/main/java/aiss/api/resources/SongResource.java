package aiss.api.resources;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.NotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import aiss.model.Song;
import aiss.model.repository.MapPlaylistRepository;
import aiss.model.repository.PlaylistRepository;

import java.net.URI;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;



@Path("/songs")
public class SongResource {

	public static SongResource _instance=null;
	PlaylistRepository repository;
	
	private SongResource(){
		repository=MapPlaylistRepository.getInstance();
	}
	
	public static SongResource getInstance()
	{
		if(_instance==null)
			_instance=new SongResource();
		return _instance; 
	}
	private boolean matchString(Song s, String filter) {
		return s.getAlbum().toUpperCase().contains(filter.toUpperCase()) ||
				s.getArtist().toUpperCase().contains(filter.toUpperCase()) ||
				s.getTitle().toUpperCase().contains(filter.toUpperCase());
	}
	@GET
	@Produces("application/json")
	public Collection<Song> getAll(@QueryParam("order") String order, @QueryParam("q") String filter, @QueryParam("limit") String limit, @QueryParam("offset") String offset)
	{
		
		Collection<Song> songs =  this.repository.getAllSongs();
		if(filter != null) {
			songs = songs.stream().filter(s -> matchString(s, filter)).collect(Collectors.toList());
		}
		if(order != null) {
			if(order.equals("album")) {
				songs = songs.stream().sorted(Comparator.comparing(Song::getAlbum)).collect(Collectors.toList());
			}else if(order.equals("-album")) {
				songs = songs.stream().sorted(Comparator.comparing(Song::getAlbum).reversed()).collect(Collectors.toList());
			}else if(order.equals("artist")) {
				songs = songs.stream().sorted(Comparator.comparing(Song::getArtist)).collect(Collectors.toList());
			}else if(order.equals("-artist")) {
				songs = songs.stream().sorted(Comparator.comparing(Song::getArtist).reversed()).collect(Collectors.toList());
			}else if(order.equals("year")) {
				songs = songs.stream().sorted(Comparator.comparing(Song::getYear)).collect(Collectors.toList());
			}else if(order.equals("-year")) {
				songs = songs.stream().sorted(Comparator.comparing(Song::getYear).reversed()).collect(Collectors.toList());

			}
			if(offset != null) {
				if(songs.size() < Integer.parseInt(offset)) {
					songs = songs.stream().collect(Collectors.toList()).subList(0, songs.size());
				}else {
					songs = songs.stream().collect(Collectors.toList()).subList(0, Integer.parseInt(offset));
				}
			}
			if(limit != null) {
				songs = songs.stream().collect(Collectors.toList()).subList(0, Integer.parseInt(limit));
			}
		}
		return songs;
	}
	
	
	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Song get(@PathParam("id") String songId)
	{
		Song song = repository.getSong(songId);
		if(song == null) throw new NotFoundException("The song of ID: " + songId + "doesn`t exist");
		return song;
	}
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response addSong(@Context UriInfo uriInfo, Song song) {
		if(song.getTitle() == null || song.getTitle().equals("")) throw new BadRequestException("The name of the song must not be empty");
		repository.addSong(song);
		UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "get");
		URI uri = ub.build(song.getId());
		ResponseBuilder respb = Response.created(uri);
		respb.entity(song);
		return respb.build();
	}
	
	
	@PUT
	@Consumes("application/json")
	public Response updateSong(Song song) {
		Song oSong = repository.getSong(song.getId());
		if (oSong == null) throw new NotFoundException("The song of ID: " + song.getId() + " doesn`t exist");
		if(song.getTitle() != null) oSong.setTitle(song.getTitle());
		if(song.getArtist() != null) oSong.setArtist(song.getArtist());
		if(song.getAlbum() != null) oSong.setAlbum(song.getAlbum());
		if(song.getYear() != null) oSong.setYear(song.getYear());
		return Response.noContent().build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response removeSong(@PathParam("id") String songId) {
		Song dSong = repository.getSong(songId);
		if(dSong == null) throw new NotFoundException("The song with id="+ songId +" was not found");
		repository.deleteSong(songId);
		return Response.noContent().build();
		}
	
}
