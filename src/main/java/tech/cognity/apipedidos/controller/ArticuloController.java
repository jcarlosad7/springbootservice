package tech.cognity.apipedidos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tech.cognity.apipedidos.converter.ArticuloConverter;
import tech.cognity.apipedidos.dto.ArticuloDTO;
import tech.cognity.apipedidos.entity.Articulo;
import tech.cognity.apipedidos.service.ArticuloService;
import tech.cognity.apipedidos.util.WrapperResponse;

@RestController
@RequestMapping("/v1/articulos")
public class ArticuloController {
	@Autowired
	private ArticuloService service;
	
	@Autowired
	private ArticuloConverter converter;
	
	@GetMapping()
	public ResponseEntity<List<ArticuloDTO>> findAll(
			@RequestParam(value="nombre", required=false) String nombre,
			@RequestParam(value="offset",required=false, defaultValue = "0") int pageNumber,
			@RequestParam(value="limit",required=false, defaultValue = "5") int pageSize){
		
		Pageable pagina=PageRequest.of(pageNumber, pageSize);
		List<Articulo> registros;
		if(nombre==null) {
			registros=service.findAll(pagina);
		}else {
			registros=service.findByNombre(nombre, pagina);
		}
		List<ArticuloDTO> registrosDTO=converter.fromEntity(registros);
		
		return new WrapperResponse(true,"success",registrosDTO).createResponse(HttpStatus.OK);
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<WrapperResponse<ArticuloDTO>> findById(@PathVariable("id") int id){
		Articulo registro=service.findById(id);
		ArticuloDTO registroDTO= converter.fromEntity(registro);
		if(registro==null) {
			return ResponseEntity.notFound().build();
		}
		return new WrapperResponse(true,"success",registroDTO).createResponse(HttpStatus.OK);
	}
	
	@PostMapping()
	public ResponseEntity<ArticuloDTO> create (@RequestBody ArticuloDTO articulo){
		Articulo registro = service.save(converter.fromDTO(articulo));
		return new WrapperResponse(true,"success",converter.fromEntity(registro)).createResponse(HttpStatus.CREATED);
	}
	
	@PutMapping(value="/{id}")
	public ResponseEntity<ArticuloDTO> update(@PathVariable("id") int id, @RequestBody ArticuloDTO articulo){
		Articulo registro=service.update(converter.fromDTO(articulo));
		if(registro==null) {
			return ResponseEntity.notFound().build();
		}
		return new WrapperResponse(true,"success",converter.fromEntity(registro)).createResponse(HttpStatus.OK);
	}
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<ArticuloDTO> delete(@PathVariable("id") int id){
		service.delete(id);
		return new WrapperResponse(true,"success",null).createResponse(HttpStatus.OK);
	}
	
}
