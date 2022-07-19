package dio.libdesignpatternsspring.service.impl;

import dio.libdesignpatternsspring.model.Address;
import dio.libdesignpatternsspring.model.AddressRepo;
import dio.libdesignpatternsspring.model.Client;
import dio.libdesignpatternsspring.model.ClientRepo;
import dio.libdesignpatternsspring.service.ClientService;
import dio.libdesignpatternsspring.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {
	@Autowired
	private ClientRepo clientRepo;

	@Autowired
	private AddressRepo addressRepo;
	@Autowired
	private ViaCepService viaCepService;

	private void saveClient(Client client) {
		String cep = client.getAddress().getCep();
		Address address = addressRepo.findById(cep).orElseGet(() -> {
			Address newAddress = viaCepService.queryCep(cep);
			addressRepo.save(newAddress);
			return newAddress;
		});
		client.setAddress(address);
		clientRepo.save(client);
	}

	@Override
	public Iterable<Client> findAll() {
		return clientRepo.findAll();
	}

	@Override
	public Client findById(Long id) {
		Optional<Client> client = clientRepo.findById(id);
		return client.get();
	}

	@Override
	public void insert(Client client) {
		saveClient(client);

	}

	@Override
	public void update(Long id, Client client) {
		Optional<Client> dBClient = clientRepo.findById(id);
		if (dBClient.isPresent()) saveClient(client);
	}

	@Override
	public void delete(Long id) {
		clientRepo.deleteById(id);
	}

}
